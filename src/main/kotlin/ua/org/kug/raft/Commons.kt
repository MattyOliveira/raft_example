package ua.org.kug.raft

import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.withTimeout
import mu.KotlinLogging
import java.util.*
import kotlin.concurrent.schedule

class TemporizadorContagemRegressivaReinicializavel(private val action: suspend () -> Unit) {

    private val log = KotlinLogging.logger("Cronometro")

    private var cronometro = iniciarCronometro()

    fun resetar() {
        log.info { "Cronometro reiniciado" }
        cronometro.cancel()
        cronometro = iniciarCronometro()
    }

    private fun iniciarCronometro(): Timer {
        val time = (20_000..23_000).aleatorio().toLong()
        val newTimer = Timer()
        newTimer.schedule(time) {
            runBlocking { action() }
        }
        return newTimer
    }
}

fun ClosedRange<Int>.aleatorio() =
        Random().nextInt((endInclusive + 1) - start) + start


suspend fun <T> tentarNovamente(delay: Long = 5000, block: suspend () -> T): T {
    while (true) {
        try {
            return block()
        } catch (e: Exception) {
            // nothing
        }
        delay(delay)
    }
}

class Log<T> {

    var ultimoIndex = 0

    private val log = mutableListOf<T>()

    fun recuperar(i: Int): T =
            if (ultimoIndex - 1 < i) throw IndexOutOfBoundsException() else log[i]

    fun adicionar(i: Int, entry: T) =
         when {
             ultimoIndex == i -> {
                 ultimoIndex += 1
                log.add(entry)
            }
             ultimoIndex < i -> false
            else -> {
                log[i] = entry
                ultimoIndex = i + 1
                true
            }
        }

    fun entradas() = log.subList(0, ultimoIndex)
}

fun main(args: Array<String>) = runBlocking {

    withTimeout(10L) {
        delay(15)
        print("Oi")
    }
}