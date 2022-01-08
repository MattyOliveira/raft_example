package ua.org.kug.raft

import ua.org.kug.raft.RaftGrpc.*

import io.grpc.*
import io.grpc.stub.*

import kotlinx.coroutines.experimental.channels.Channel as KtChannel
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.*
import kotlin.coroutines.experimental.*


@javax.annotation.Generated(
    value = ["by gRPC Kotlin generator"],
    comments = "Source: greeter.proto"
)
object RaftGrpcKt {

    /**
     * Creates a new coroutine stub that supports all call types for the service
     */
    @JvmStatic
    fun newStub(channel: Channel): RaftKtStub {
        return RaftKtStub(channel)
    }

    class RaftKtStub : AbstractStub<RaftKtStub> {

        private val delegate: ua.org.kug.raft.RaftGrpc.RaftStub

        internal constructor(channel: Channel) : super(channel) {
            delegate = ua.org.kug.raft.RaftGrpc.newStub(channel)
        }

        internal constructor(channel: Channel, callOptions: CallOptions) : super(channel, callOptions) {
            delegate = ua.org.kug.raft.RaftGrpc.newStub(channel).build(channel, callOptions)
        }

        override fun build(channel: Channel, callOptions: CallOptions): RaftKtStub {
            return RaftKtStub(channel, callOptions)
        }

        
        
        
        suspend fun vote(request: ua.org.kug.raft.RequestVoteRPC): ua.org.kug.raft.ResponseVoteRPC {
            return suspendCoroutine { cont ->
                delegate.vote(request, ContinuationStreamObserver(cont))
            }
        }
        
        
        
        suspend fun append(request: ua.org.kug.raft.RequestAppendEntriesRPC): ua.org.kug.raft.ResponseAppendEntriesRPC {
            return suspendCoroutine { cont ->
                delegate.append(request, ContinuationStreamObserver(cont))
            }
        }
    }

    
    abstract class RaftImplBase(
        override val coroutineContext: CoroutineContext = Dispatchers.Default,
        val sendChannelCapacity: Int = KtChannel.UNLIMITED
    ) : BindableService, CoroutineScope {

        
        
        
        open suspend fun vote(request: ua.org.kug.raft.RequestVoteRPC): ua.org.kug.raft.ResponseVoteRPC {
            throw unimplemented(getVoteMethod()).asRuntimeException()
        }

        internal fun voteInternal(
            request: ua.org.kug.raft.RequestVoteRPC,
            responseObserver: StreamObserver<ua.org.kug.raft.ResponseVoteRPC>
        ) {
            launch {
                tryCatchingStatus(responseObserver) {
                    val response = vote(request)
                    onNext(response)
                }
            }
        }
        
        
        
        open suspend fun append(request: ua.org.kug.raft.RequestAppendEntriesRPC): ua.org.kug.raft.ResponseAppendEntriesRPC {
            throw unimplemented(getAppendMethod()).asRuntimeException()
        }

        internal fun appendInternal(
            request: ua.org.kug.raft.RequestAppendEntriesRPC,
            responseObserver: StreamObserver<ua.org.kug.raft.ResponseAppendEntriesRPC>
        ) {
            launch {
                tryCatchingStatus(responseObserver) {
                    val response = append(request)
                    onNext(response)
                }
            }
        }

        override fun bindService(): ServerServiceDefinition {
            return ServerServiceDefinition.builder(getServiceDescriptor())
                .addMethod(
                    getVoteMethod(),
                    ServerCalls.asyncUnaryCall(
                        MethodHandlers(this, METHODID_VOTE)
                    )
                )
                .addMethod(
                    getAppendMethod(),
                    ServerCalls.asyncUnaryCall(
                        MethodHandlers(this, METHODID_APPEND)
                    )
                )
                .build()
        }
    }

    private fun unimplemented(methodDescriptor: MethodDescriptor<*, *>): Status {
        return Status.UNIMPLEMENTED
            .withDescription(
                String.format(
                    "Method %s is unimplemented",
                    methodDescriptor.fullMethodName
                )
            )
    }

    private fun <E> handleException(t: Throwable?, responseObserver: StreamObserver<E>) {
        when (t) {
            null -> return
            is CancellationException -> handleException(t.cause, responseObserver)
            is StatusException, is StatusRuntimeException -> responseObserver.onError(t)
            is RuntimeException -> {
                responseObserver.onError(Status.UNKNOWN.asRuntimeException())
                throw t
            }
            is Exception -> {
                responseObserver.onError(Status.UNKNOWN.asException())
                throw t
            }
            else -> {
                responseObserver.onError(Status.INTERNAL.asException())
                throw t
            }
        }
    }

    private suspend fun <E> tryCatchingStatus(responseObserver: StreamObserver<E>, body: suspend StreamObserver<E>.() -> Unit) {
        try {
            responseObserver.body()
            responseObserver.onCompleted()
        } catch (t: Throwable) {
            handleException(t, responseObserver)
        }
    }

    class ManyToOneCall<in TRequest, out TResponse>(
        private val request: StreamObserver<TRequest>,
        private val response: Deferred<TResponse>
    ) : StreamObserverSendAdapter<TRequest>(request),
        Deferred<TResponse> by response

    class ManyToManyCall<in TRequest, out TResponse>(
        private val request: StreamObserver<TRequest>,
        private val response: ReceiveChannel<TResponse>
    ) : StreamObserverSendAdapter<TRequest>(request),
        ReceiveChannel<TResponse> by response

    open class StreamObserverSendAdapter<in E>(private val streamObserver: StreamObserver<E>) {

        fun close(cause: Throwable? = null): Boolean {
            if (cause != null) {
                streamObserver.onError(cause)
            } else {
                streamObserver.onCompleted()
            }

            return true
        }

        fun send(element: E) {
            streamObserver.onNext(element)
        }
    }

    private class ContinuationStreamObserver<E>(
        private val continuation: Continuation<E>
    ) : StreamObserver<E> {
        override fun onNext(value: E) { continuation.resume(value) }
        override fun onError(t: Throwable) { continuation.resumeWithException(t) }
        override fun onCompleted() { }
    }

    private class StreamObserverDeferred<E>(
        private val deferred: CompletableDeferred<E> = CompletableDeferred()
    ) : StreamObserver<E>, Deferred<E> by deferred {

        override fun onNext(value: E) { deferred.complete(value) }
        override fun onError(t: Throwable) { deferred.cancel(t) }
        override fun onCompleted() { /* nothing */ }
    }

    private class StreamObserverChannel<E>(
        private val channel: KtChannel<E> = KtChannel<E>(KtChannel.UNLIMITED)
    ) : StreamObserver<E>, ReceiveChannel<E> by channel {

        override fun onNext(value: E) { channel.offer(value) }
        override fun onError(t: Throwable?) { channel.close(cause = t) }
        override fun onCompleted() { channel.close(cause = null) }
    }

    val METHODID_VOTE = 0
    val METHODID_APPEND = 1

    private class MethodHandlers<Req, Resp> internal constructor(
        private val serviceImpl: RaftImplBase,
        private val methodId: Int
    ) : ServerCalls.UnaryMethod<Req, Resp>,
        ServerCalls.ServerStreamingMethod<Req, Resp>,
        ServerCalls.ClientStreamingMethod<Req, Resp>,
        ServerCalls.BidiStreamingMethod<Req, Resp> {

        @Suppress("UNCHECKED_CAST")
        override fun invoke(request: Req, responseObserver: StreamObserver<Resp>) {
            when (methodId) {
                METHODID_VOTE ->
                    serviceImpl.voteInternal(
                        request as ua.org.kug.raft.RequestVoteRPC,
                        responseObserver as StreamObserver<ua.org.kug.raft.ResponseVoteRPC>
                    )
                METHODID_APPEND ->
                    serviceImpl.appendInternal(
                        request as ua.org.kug.raft.RequestAppendEntriesRPC,
                        responseObserver as StreamObserver<ua.org.kug.raft.ResponseAppendEntriesRPC>
                    )
                else -> throw AssertionError()
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun invoke(responseObserver: StreamObserver<Resp>): StreamObserver<Req> {
            when (methodId) {
                else -> throw AssertionError()
            }
        }
    }
}
