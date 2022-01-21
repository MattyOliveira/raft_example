package ua.org.kug.raft

import io.grpc.ServerBuilder
import io.javalin.Javalin
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.consumeEach
import mu.KotlinLogging
import ua.org.kug.raft.RequestAppendEntriesRPC.LogEntry
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.fixedRateTimer


enum class Estados {
    SEGUIDOR, CANDIDATO, LIDER
}

class RaftServer(val id: Int, val servidores: List<RaftClient>) :
        RaftGrpcKt.RaftImplBase(
                coroutineContext = newFixedThreadPoolContext(4, "server-pool"),
                sendChannelCapacity = 4) {

    val kLogger = KotlinLogging.logger("Servidor")

    @Volatile
    var termoAtual = 0

    @Volatile
    var votadoEm = -1

    @Volatile
    var estado = Estados.SEGUIDOR

    private val maioria = servidores.size / 2 + 1

    private var commitIndex = 0

    private val log = Log<LogEntry>()

    private val canal = Channel<Estados>()

    init {
        ktorServer()

        kLogger.info { "Servidor $id é dentro $estado no termo $termoAtual" }

        val aguardandoEstadoDoLider = aguardandoEstadoDoLider()

        launch {
            canal.consumeEach {
                when (it) {
                    Estados.SEGUIDOR -> aguardandoEstadoDoLider.resetar()
                    Estados.CANDIDATO -> eleicaoLider()
                    Estados.LIDER -> anexarPedidoEstadoDoLider()
                }
            }
        }
    }

    private fun ktorServer() {
        val servidor = embeddedServer(Netty, port = 7000 + id) {
            routing {
                get("/") {
                    call.respondText("Servidor $id log ${entradas()}", ContentType.Text.Plain)
                }
                get("/cmd/{command}") {
                    anexarComando(call.parameters["command"]!!)
                    call.respondText("Servidor $id log ${entradas()}", ContentType.Text.Plain)
                }
            }
        }
        servidor.start(wait = false)
    }

    fun entradas() =
            log.entradas().map { "${it.term}: ${it.command}"}

    fun anexarComando(comando: String): String {
        val logEntry = LogEntry.newBuilder()
                .setTerm(termoAtual)
                .setCommand(comando)
                .build()
        log.adicionar(log.ultimoIndex, logEntry)
        return comando
    }

     private fun anexarPedidoEstadoDoLider() {
        kLogger.info { "Lider eleito $id" }

        val proximoIndex = Array(servidores.size) { _ -> commitIndex + 1 }
        val indexDePartida = Array(servidores.size) { _ -> 0 }

        fixedRateTimer(period = 2000) {
            runBlocking {
            if (estado == Estados.SEGUIDOR) cancel()

            println(proximoIndex.toList())
            println(indexDePartida.toList())

            servidores.forEach {
                launch {
                    try {
                        val entradas = mutableListOf<LogEntry>()
                        val i = proximoIndex[it.id - 1]
                        val indexLogAnterior = i - 2
                        val termoLogAnterior = if (indexLogAnterior >= 0) log.recuperar(indexLogAnterior).term else -1

                        if (log.ultimoIndex >= proximoIndex[it.id - 1]) {
                            entradas.add(log.recuperar(i - 1))
                        }

                        println("Anexo id: ${it.id} termo atual: ${termoAtual} " +
                                    "Index do log anterior ${indexLogAnterior} Log anterior ${termoLogAnterior} entradas ${entradas} indice do commit ${commitIndex}")

                        val response = it.anexar(
                                idLider = id,
                                termo = termoAtual,
                                indexLogAnterior = indexLogAnterior,
                                termoLogAnterior = termoLogAnterior,
                                entradas = entradas,
                                liderCompromisso = commitIndex)


                        if (response.term > termoAtual) {
                            termoAtual = response.term
                            estado = Estados.SEGUIDOR
                            kLogger.info {
                                "Servidor $id convertido em $estado no termo $termoAtual"
                            }
                            launch { canal.offer(estado) }
                            return@launch
                        }

                        if (response.success) {
                            if (entradas.size > 0) {
                                proximoIndex[it.id - 1] += 1
                                indexDePartida[it.id - 1] += 1

                                val count = indexDePartida.filter { it > commitIndex }.count()
                                if (count >= maioria) commitIndex += 1
                            } else {
                                indexDePartida[it.id - 1] = indexLogAnterior + 1
                            }
                        } else {
                                proximoIndex[it.id - 1] -= 1
                        }

                    } catch (e: Exception) {
                        kLogger.info { "Servidor ${it.id} ${e.message}" }
                    }
                }
            }
        }
        }

    }

    private fun aguardandoEstadoDoLider() =
        TemporizadorContagemRegressivaReinicializavel {
                estado = Estados.CANDIDATO
                kLogger.info { "Servidor $id é dentro $estado no termo $termoAtual" }
                canal.offer(estado)
            }

    private suspend fun eleicaoLider() {

        val tempoLimiteEleicao = 25L

        while (estado == Estados.CANDIDATO) {
            termoAtual += 1
            votadoEm = id
            val votosConcedidos = AtomicInteger()
            kLogger.info { "termo: $termoAtual" }
            val contagemRegressivaTrava = CountDownLatch(maioria)
            coroutineScope {
                servidores.forEach {
                    launch {
                        val responseVote = tentarNovamente {
                            val lastIndex = log.ultimoIndex
                            val lastTerm = if (lastIndex == 0) 0 else log.recuperar(log.ultimoIndex - 1).term
                            it.voto(
                                    termoAtual,
                                    id,
                                    lastIndex,
                                    lastTerm)
                        }
                        contagemRegressivaTrava.countDown()
                        if (termoAtual < responseVote.term) estado = Estados.SEGUIDOR
                        if (responseVote.voteGranted) votosConcedidos.incrementAndGet()
                    }
                }
                contagemRegressivaTrava.await(tempoLimiteEleicao, TimeUnit.SECONDS)
                coroutineContext.cancelChildren()
            }

            if (estado == Estados.CANDIDATO && votosConcedidos.get() >= maioria)
                estado = Estados.LIDER
            else if (estado == Estados.CANDIDATO)
                delay((2_000..3_000).random().toLong())
            kLogger.info { "Servidor $id está $estado no termo $termoAtual votos ${votosConcedidos.get()}" }
        }

        launch { canal.send(estado) }
    }

    override suspend fun vote(request: RequestVoteRPC): ResponseVoteRPC {
        val granted = if (request.term < termoAtual) false
        else if (termoAtual == request.term) votadoEm == request.candidateId
        else {
            if (log.ultimoIndex >= 1 &&
                    request.lastLogTerm < log.recuperar(log.ultimoIndex - 1).term) false
            else if (log.ultimoIndex >= 1 &&
                    request.lastLogTerm == log.recuperar(log.ultimoIndex - 1).term &&
                    request.lastLogIndex < log.ultimoIndex) false
            else {
                termoAtual = request.term
                votadoEm = request.candidateId
                estado = Estados.SEGUIDOR
                launch { canal.send(estado) }
                true
            }
        }

        return ResponseVoteRPC.newBuilder()
                    .setTerm(termoAtual)
                    .setVoteGranted(granted)
                    .build()

    }

    override suspend fun append(request: RequestAppendEntriesRPC):
            ResponseAppendEntriesRPC {
        kLogger.info { "Lider propagação de dados: ${request.leaderId} termo: ${request.term}" }

        if (request.term > termoAtual) {
            termoAtual = request.term
            votadoEm = -1
            estado = Estados.SEGUIDOR
            launch {  canal.send(estado) }
        }

        if (request.leaderId != id) {
            estado = Estados.SEGUIDOR
            launch {  canal.send(estado) }

        }

        if (request.leaderCommit > commitIndex) {
            commitIndex = minOf(request.leaderCommit, log.ultimoIndex)
        }

        val success = request.prevLogIndex == -1 ||
                (log.ultimoIndex > request.prevLogIndex &&
                log.recuperar(request.prevLogIndex).term == request.prevLogTerm)

        if (success && request.entriesCount > 0) log.adicionar(request.prevLogIndex + 1, request.getEntries(0))

        kLogger.info { "Estado: termo ${termoAtual} ordemDeEnvio: $commitIndex " }

        return ResponseAppendEntriesRPC.newBuilder()
                    .setTerm(termoAtual)
                    .setSuccess(success)
                    .build()

    }
}

private fun raftInstance(id: Int, porta: Int, servidores: List<RaftClient>) {
    val log = KotlinLogging.logger("servidor")

    log.info { "Iniciando servidor $id na $porta" }

    ServerBuilder.forPort(porta)
            .addService(RaftServer(id, servidores))
            .build()
            .start()
            .awaitTermination()
}

fun main(args: Array<String>) {
    val raftClient1 = RaftClient(porta = 8081, id = 1)
    val raftClient2 = RaftClient(porta = 8082, id = 2)
    val raftClient3 = RaftClient(porta = 8083, id = 3)

    val servidores = listOf(raftClient1, raftClient2, raftClient3)


    raftInstance(id = 3, porta = 8083, servidores = servidores)
}