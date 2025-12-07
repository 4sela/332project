package masterworker;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * These services are provided by Worker which Master will call and use
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.58.0)",
    comments = "Source: common.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class WorkerServiceGrpc {

  private WorkerServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "masterworker.WorkerService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<masterworker.Common.PartitionsMessage,
      masterworker.Common.Ack> getReceivePartitionsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ReceivePartitions",
      requestType = masterworker.Common.PartitionsMessage.class,
      responseType = masterworker.Common.Ack.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<masterworker.Common.PartitionsMessage,
      masterworker.Common.Ack> getReceivePartitionsMethod() {
    io.grpc.MethodDescriptor<masterworker.Common.PartitionsMessage, masterworker.Common.Ack> getReceivePartitionsMethod;
    if ((getReceivePartitionsMethod = WorkerServiceGrpc.getReceivePartitionsMethod) == null) {
      synchronized (WorkerServiceGrpc.class) {
        if ((getReceivePartitionsMethod = WorkerServiceGrpc.getReceivePartitionsMethod) == null) {
          WorkerServiceGrpc.getReceivePartitionsMethod = getReceivePartitionsMethod =
              io.grpc.MethodDescriptor.<masterworker.Common.PartitionsMessage, masterworker.Common.Ack>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ReceivePartitions"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  masterworker.Common.PartitionsMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  masterworker.Common.Ack.getDefaultInstance()))
              .setSchemaDescriptor(new WorkerServiceMethodDescriptorSupplier("ReceivePartitions"))
              .build();
        }
      }
    }
    return getReceivePartitionsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<masterworker.Common.PhaseCommand,
      masterworker.Common.Ack> getStartPhaseMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "StartPhase",
      requestType = masterworker.Common.PhaseCommand.class,
      responseType = masterworker.Common.Ack.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<masterworker.Common.PhaseCommand,
      masterworker.Common.Ack> getStartPhaseMethod() {
    io.grpc.MethodDescriptor<masterworker.Common.PhaseCommand, masterworker.Common.Ack> getStartPhaseMethod;
    if ((getStartPhaseMethod = WorkerServiceGrpc.getStartPhaseMethod) == null) {
      synchronized (WorkerServiceGrpc.class) {
        if ((getStartPhaseMethod = WorkerServiceGrpc.getStartPhaseMethod) == null) {
          WorkerServiceGrpc.getStartPhaseMethod = getStartPhaseMethod =
              io.grpc.MethodDescriptor.<masterworker.Common.PhaseCommand, masterworker.Common.Ack>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "StartPhase"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  masterworker.Common.PhaseCommand.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  masterworker.Common.Ack.getDefaultInstance()))
              .setSchemaDescriptor(new WorkerServiceMethodDescriptorSupplier("StartPhase"))
              .build();
        }
      }
    }
    return getStartPhaseMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static WorkerServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WorkerServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WorkerServiceStub>() {
        @java.lang.Override
        public WorkerServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WorkerServiceStub(channel, callOptions);
        }
      };
    return WorkerServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static WorkerServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WorkerServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WorkerServiceBlockingStub>() {
        @java.lang.Override
        public WorkerServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WorkerServiceBlockingStub(channel, callOptions);
        }
      };
    return WorkerServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static WorkerServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WorkerServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WorkerServiceFutureStub>() {
        @java.lang.Override
        public WorkerServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WorkerServiceFutureStub(channel, callOptions);
        }
      };
    return WorkerServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * These services are provided by Worker which Master will call and use
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * For Master to send partition boundaries
     * </pre>
     */
    default void receivePartitions(masterworker.Common.PartitionsMessage request,
        io.grpc.stub.StreamObserver<masterworker.Common.Ack> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReceivePartitionsMethod(), responseObserver);
    }

    /**
     * <pre>
     * For Master to tell worker to start a phase
     * </pre>
     */
    default void startPhase(masterworker.Common.PhaseCommand request,
        io.grpc.stub.StreamObserver<masterworker.Common.Ack> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getStartPhaseMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service WorkerService.
   * <pre>
   * These services are provided by Worker which Master will call and use
   * </pre>
   */
  public static abstract class WorkerServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return WorkerServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service WorkerService.
   * <pre>
   * These services are provided by Worker which Master will call and use
   * </pre>
   */
  public static final class WorkerServiceStub
      extends io.grpc.stub.AbstractAsyncStub<WorkerServiceStub> {
    private WorkerServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WorkerServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WorkerServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * For Master to send partition boundaries
     * </pre>
     */
    public void receivePartitions(masterworker.Common.PartitionsMessage request,
        io.grpc.stub.StreamObserver<masterworker.Common.Ack> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getReceivePartitionsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * For Master to tell worker to start a phase
     * </pre>
     */
    public void startPhase(masterworker.Common.PhaseCommand request,
        io.grpc.stub.StreamObserver<masterworker.Common.Ack> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getStartPhaseMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service WorkerService.
   * <pre>
   * These services are provided by Worker which Master will call and use
   * </pre>
   */
  public static final class WorkerServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<WorkerServiceBlockingStub> {
    private WorkerServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WorkerServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WorkerServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * For Master to send partition boundaries
     * </pre>
     */
    public masterworker.Common.Ack receivePartitions(masterworker.Common.PartitionsMessage request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getReceivePartitionsMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * For Master to tell worker to start a phase
     * </pre>
     */
    public masterworker.Common.Ack startPhase(masterworker.Common.PhaseCommand request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getStartPhaseMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service WorkerService.
   * <pre>
   * These services are provided by Worker which Master will call and use
   * </pre>
   */
  public static final class WorkerServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<WorkerServiceFutureStub> {
    private WorkerServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WorkerServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WorkerServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * For Master to send partition boundaries
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<masterworker.Common.Ack> receivePartitions(
        masterworker.Common.PartitionsMessage request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getReceivePartitionsMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * For Master to tell worker to start a phase
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<masterworker.Common.Ack> startPhase(
        masterworker.Common.PhaseCommand request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getStartPhaseMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_RECEIVE_PARTITIONS = 0;
  private static final int METHODID_START_PHASE = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_RECEIVE_PARTITIONS:
          serviceImpl.receivePartitions((masterworker.Common.PartitionsMessage) request,
              (io.grpc.stub.StreamObserver<masterworker.Common.Ack>) responseObserver);
          break;
        case METHODID_START_PHASE:
          serviceImpl.startPhase((masterworker.Common.PhaseCommand) request,
              (io.grpc.stub.StreamObserver<masterworker.Common.Ack>) responseObserver);
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

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getReceivePartitionsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              masterworker.Common.PartitionsMessage,
              masterworker.Common.Ack>(
                service, METHODID_RECEIVE_PARTITIONS)))
        .addMethod(
          getStartPhaseMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              masterworker.Common.PhaseCommand,
              masterworker.Common.Ack>(
                service, METHODID_START_PHASE)))
        .build();
  }

  private static abstract class WorkerServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    WorkerServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return masterworker.Common.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("WorkerService");
    }
  }

  private static final class WorkerServiceFileDescriptorSupplier
      extends WorkerServiceBaseDescriptorSupplier {
    WorkerServiceFileDescriptorSupplier() {}
  }

  private static final class WorkerServiceMethodDescriptorSupplier
      extends WorkerServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    WorkerServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (WorkerServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new WorkerServiceFileDescriptorSupplier())
              .addMethod(getReceivePartitionsMethod())
              .addMethod(getStartPhaseMethod())
              .build();
        }
      }
    }
    return result;
  }
}
