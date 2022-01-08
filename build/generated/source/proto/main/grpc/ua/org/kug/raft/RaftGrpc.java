package ua.org.kug.raft;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.1)",
    comments = "Source: greeter.proto")
public final class RaftGrpc {

  private RaftGrpc() {}

  public static final String SERVICE_NAME = "ua.org.kug.raft.Raft";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<ua.org.kug.raft.RequestVoteRPC,
      ua.org.kug.raft.ResponseVoteRPC> getVoteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Vote",
      requestType = ua.org.kug.raft.RequestVoteRPC.class,
      responseType = ua.org.kug.raft.ResponseVoteRPC.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ua.org.kug.raft.RequestVoteRPC,
      ua.org.kug.raft.ResponseVoteRPC> getVoteMethod() {
    io.grpc.MethodDescriptor<ua.org.kug.raft.RequestVoteRPC, ua.org.kug.raft.ResponseVoteRPC> getVoteMethod;
    if ((getVoteMethod = RaftGrpc.getVoteMethod) == null) {
      synchronized (RaftGrpc.class) {
        if ((getVoteMethod = RaftGrpc.getVoteMethod) == null) {
          RaftGrpc.getVoteMethod = getVoteMethod = 
              io.grpc.MethodDescriptor.<ua.org.kug.raft.RequestVoteRPC, ua.org.kug.raft.ResponseVoteRPC>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "ua.org.kug.raft.Raft", "Vote"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ua.org.kug.raft.RequestVoteRPC.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ua.org.kug.raft.ResponseVoteRPC.getDefaultInstance()))
                  .setSchemaDescriptor(new RaftMethodDescriptorSupplier("Vote"))
                  .build();
          }
        }
     }
     return getVoteMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ua.org.kug.raft.RequestAppendEntriesRPC,
      ua.org.kug.raft.ResponseAppendEntriesRPC> getAppendMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Append",
      requestType = ua.org.kug.raft.RequestAppendEntriesRPC.class,
      responseType = ua.org.kug.raft.ResponseAppendEntriesRPC.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ua.org.kug.raft.RequestAppendEntriesRPC,
      ua.org.kug.raft.ResponseAppendEntriesRPC> getAppendMethod() {
    io.grpc.MethodDescriptor<ua.org.kug.raft.RequestAppendEntriesRPC, ua.org.kug.raft.ResponseAppendEntriesRPC> getAppendMethod;
    if ((getAppendMethod = RaftGrpc.getAppendMethod) == null) {
      synchronized (RaftGrpc.class) {
        if ((getAppendMethod = RaftGrpc.getAppendMethod) == null) {
          RaftGrpc.getAppendMethod = getAppendMethod = 
              io.grpc.MethodDescriptor.<ua.org.kug.raft.RequestAppendEntriesRPC, ua.org.kug.raft.ResponseAppendEntriesRPC>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "ua.org.kug.raft.Raft", "Append"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ua.org.kug.raft.RequestAppendEntriesRPC.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ua.org.kug.raft.ResponseAppendEntriesRPC.getDefaultInstance()))
                  .setSchemaDescriptor(new RaftMethodDescriptorSupplier("Append"))
                  .build();
          }
        }
     }
     return getAppendMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RaftStub newStub(io.grpc.Channel channel) {
    return new RaftStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RaftBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new RaftBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RaftFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new RaftFutureStub(channel);
  }

  /**
   */
  public static abstract class RaftImplBase implements io.grpc.BindableService {

    /**
     */
    public void vote(ua.org.kug.raft.RequestVoteRPC request,
        io.grpc.stub.StreamObserver<ua.org.kug.raft.ResponseVoteRPC> responseObserver) {
      asyncUnimplementedUnaryCall(getVoteMethod(), responseObserver);
    }

    /**
     */
    public void append(ua.org.kug.raft.RequestAppendEntriesRPC request,
        io.grpc.stub.StreamObserver<ua.org.kug.raft.ResponseAppendEntriesRPC> responseObserver) {
      asyncUnimplementedUnaryCall(getAppendMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getVoteMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                ua.org.kug.raft.RequestVoteRPC,
                ua.org.kug.raft.ResponseVoteRPC>(
                  this, METHODID_VOTE)))
          .addMethod(
            getAppendMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                ua.org.kug.raft.RequestAppendEntriesRPC,
                ua.org.kug.raft.ResponseAppendEntriesRPC>(
                  this, METHODID_APPEND)))
          .build();
    }
  }

  /**
   */
  public static final class RaftStub extends io.grpc.stub.AbstractStub<RaftStub> {
    private RaftStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RaftStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RaftStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RaftStub(channel, callOptions);
    }

    /**
     */
    public void vote(ua.org.kug.raft.RequestVoteRPC request,
        io.grpc.stub.StreamObserver<ua.org.kug.raft.ResponseVoteRPC> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getVoteMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void append(ua.org.kug.raft.RequestAppendEntriesRPC request,
        io.grpc.stub.StreamObserver<ua.org.kug.raft.ResponseAppendEntriesRPC> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAppendMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class RaftBlockingStub extends io.grpc.stub.AbstractStub<RaftBlockingStub> {
    private RaftBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RaftBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RaftBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RaftBlockingStub(channel, callOptions);
    }

    /**
     */
    public ua.org.kug.raft.ResponseVoteRPC vote(ua.org.kug.raft.RequestVoteRPC request) {
      return blockingUnaryCall(
          getChannel(), getVoteMethod(), getCallOptions(), request);
    }

    /**
     */
    public ua.org.kug.raft.ResponseAppendEntriesRPC append(ua.org.kug.raft.RequestAppendEntriesRPC request) {
      return blockingUnaryCall(
          getChannel(), getAppendMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class RaftFutureStub extends io.grpc.stub.AbstractStub<RaftFutureStub> {
    private RaftFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RaftFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RaftFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RaftFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ua.org.kug.raft.ResponseVoteRPC> vote(
        ua.org.kug.raft.RequestVoteRPC request) {
      return futureUnaryCall(
          getChannel().newCall(getVoteMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ua.org.kug.raft.ResponseAppendEntriesRPC> append(
        ua.org.kug.raft.RequestAppendEntriesRPC request) {
      return futureUnaryCall(
          getChannel().newCall(getAppendMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_VOTE = 0;
  private static final int METHODID_APPEND = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RaftImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(RaftImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_VOTE:
          serviceImpl.vote((ua.org.kug.raft.RequestVoteRPC) request,
              (io.grpc.stub.StreamObserver<ua.org.kug.raft.ResponseVoteRPC>) responseObserver);
          break;
        case METHODID_APPEND:
          serviceImpl.append((ua.org.kug.raft.RequestAppendEntriesRPC) request,
              (io.grpc.stub.StreamObserver<ua.org.kug.raft.ResponseAppendEntriesRPC>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class RaftBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RaftBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return ua.org.kug.raft.Greeter.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Raft");
    }
  }

  private static final class RaftFileDescriptorSupplier
      extends RaftBaseDescriptorSupplier {
    RaftFileDescriptorSupplier() {}
  }

  private static final class RaftMethodDescriptorSupplier
      extends RaftBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    RaftMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (RaftGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RaftFileDescriptorSupplier())
              .addMethod(getVoteMethod())
              .addMethod(getAppendMethod())
              .build();
        }
      }
    }
    return result;
  }
}
