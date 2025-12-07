package masterworker;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * These services are provided by Master which Workers will call and use
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.58.0)",
    comments = "Source: common.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class MasterServiceGrpc {

  private MasterServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "masterworker.MasterService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<masterworker.Common.ConnectRequest,
      masterworker.Common.ConnectResponse> getConnectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Connect",
      requestType = masterworker.Common.ConnectRequest.class,
      responseType = masterworker.Common.ConnectResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<masterworker.Common.ConnectRequest,
      masterworker.Common.ConnectResponse> getConnectMethod() {
    io.grpc.MethodDescriptor<masterworker.Common.ConnectRequest, masterworker.Common.ConnectResponse> getConnectMethod;
    if ((getConnectMethod = MasterServiceGrpc.getConnectMethod) == null) {
      synchronized (MasterServiceGrpc.class) {
        if ((getConnectMethod = MasterServiceGrpc.getConnectMethod) == null) {
          MasterServiceGrpc.getConnectMethod = getConnectMethod =
              io.grpc.MethodDescriptor.<masterworker.Common.ConnectRequest, masterworker.Common.ConnectResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Connect"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  masterworker.Common.ConnectRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  masterworker.Common.ConnectResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MasterServiceMethodDescriptorSupplier("Connect"))
              .build();
        }
      }
    }
    return getConnectMethod;
  }

  private static volatile io.grpc.MethodDescriptor<masterworker.Common.SampleRequest,
      masterworker.Common.SampleResponse> getSendSampleMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendSample",
      requestType = masterworker.Common.SampleRequest.class,
      responseType = masterworker.Common.SampleResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<masterworker.Common.SampleRequest,
      masterworker.Common.SampleResponse> getSendSampleMethod() {
    io.grpc.MethodDescriptor<masterworker.Common.SampleRequest, masterworker.Common.SampleResponse> getSendSampleMethod;
    if ((getSendSampleMethod = MasterServiceGrpc.getSendSampleMethod) == null) {
      synchronized (MasterServiceGrpc.class) {
        if ((getSendSampleMethod = MasterServiceGrpc.getSendSampleMethod) == null) {
          MasterServiceGrpc.getSendSampleMethod = getSendSampleMethod =
              io.grpc.MethodDescriptor.<masterworker.Common.SampleRequest, masterworker.Common.SampleResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SendSample"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  masterworker.Common.SampleRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  masterworker.Common.SampleResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MasterServiceMethodDescriptorSupplier("SendSample"))
              .build();
        }
      }
    }
    return getSendSampleMethod;
  }

  private static volatile io.grpc.MethodDescriptor<masterworker.Common.CompleteRequest,
      masterworker.Common.CompleteResponse> getReportCompleteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ReportComplete",
      requestType = masterworker.Common.CompleteRequest.class,
      responseType = masterworker.Common.CompleteResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<masterworker.Common.CompleteRequest,
      masterworker.Common.CompleteResponse> getReportCompleteMethod() {
    io.grpc.MethodDescriptor<masterworker.Common.CompleteRequest, masterworker.Common.CompleteResponse> getReportCompleteMethod;
    if ((getReportCompleteMethod = MasterServiceGrpc.getReportCompleteMethod) == null) {
      synchronized (MasterServiceGrpc.class) {
        if ((getReportCompleteMethod = MasterServiceGrpc.getReportCompleteMethod) == null) {
          MasterServiceGrpc.getReportCompleteMethod = getReportCompleteMethod =
              io.grpc.MethodDescriptor.<masterworker.Common.CompleteRequest, masterworker.Common.CompleteResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ReportComplete"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  masterworker.Common.CompleteRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  masterworker.Common.CompleteResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MasterServiceMethodDescriptorSupplier("ReportComplete"))
              .build();
        }
      }
    }
    return getReportCompleteMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MasterServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MasterServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MasterServiceStub>() {
        @java.lang.Override
        public MasterServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MasterServiceStub(channel, callOptions);
        }
      };
    return MasterServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MasterServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MasterServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MasterServiceBlockingStub>() {
        @java.lang.Override
        public MasterServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MasterServiceBlockingStub(channel, callOptions);
        }
      };
    return MasterServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MasterServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MasterServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MasterServiceFutureStub>() {
        @java.lang.Override
        public MasterServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MasterServiceFutureStub(channel, callOptions);
        }
      };
    return MasterServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * These services are provided by Master which Workers will call and use
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * So workers can connect and get an assigned ID
     * </pre>
     */
    default void connect(masterworker.Common.ConnectRequest request,
        io.grpc.stub.StreamObserver<masterworker.Common.ConnectResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getConnectMethod(), responseObserver);
    }

    /**
     * <pre>
     * For workers to sends sample data
     * </pre>
     */
    default void sendSample(masterworker.Common.SampleRequest request,
        io.grpc.stub.StreamObserver<masterworker.Common.SampleResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSendSampleMethod(), responseObserver);
    }

    /**
     * <pre>
     * For workers to report phase completion
     * </pre>
     */
    default void reportComplete(masterworker.Common.CompleteRequest request,
        io.grpc.stub.StreamObserver<masterworker.Common.CompleteResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReportCompleteMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service MasterService.
   * <pre>
   * These services are provided by Master which Workers will call and use
   * </pre>
   */
  public static abstract class MasterServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return MasterServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service MasterService.
   * <pre>
   * These services are provided by Master which Workers will call and use
   * </pre>
   */
  public static final class MasterServiceStub
      extends io.grpc.stub.AbstractAsyncStub<MasterServiceStub> {
    private MasterServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MasterServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MasterServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * So workers can connect and get an assigned ID
     * </pre>
     */
    public void connect(masterworker.Common.ConnectRequest request,
        io.grpc.stub.StreamObserver<masterworker.Common.ConnectResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getConnectMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * For workers to sends sample data
     * </pre>
     */
    public void sendSample(masterworker.Common.SampleRequest request,
        io.grpc.stub.StreamObserver<masterworker.Common.SampleResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSendSampleMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * For workers to report phase completion
     * </pre>
     */
    public void reportComplete(masterworker.Common.CompleteRequest request,
        io.grpc.stub.StreamObserver<masterworker.Common.CompleteResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getReportCompleteMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service MasterService.
   * <pre>
   * These services are provided by Master which Workers will call and use
   * </pre>
   */
  public static final class MasterServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<MasterServiceBlockingStub> {
    private MasterServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MasterServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MasterServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * So workers can connect and get an assigned ID
     * </pre>
     */
    public masterworker.Common.ConnectResponse connect(masterworker.Common.ConnectRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getConnectMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * For workers to sends sample data
     * </pre>
     */
    public masterworker.Common.SampleResponse sendSample(masterworker.Common.SampleRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSendSampleMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * For workers to report phase completion
     * </pre>
     */
    public masterworker.Common.CompleteResponse reportComplete(masterworker.Common.CompleteRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getReportCompleteMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service MasterService.
   * <pre>
   * These services are provided by Master which Workers will call and use
   * </pre>
   */
  public static final class MasterServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<MasterServiceFutureStub> {
    private MasterServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MasterServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MasterServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * So workers can connect and get an assigned ID
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<masterworker.Common.ConnectResponse> connect(
        masterworker.Common.ConnectRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getConnectMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * For workers to sends sample data
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<masterworker.Common.SampleResponse> sendSample(
        masterworker.Common.SampleRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSendSampleMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * For workers to report phase completion
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<masterworker.Common.CompleteResponse> reportComplete(
        masterworker.Common.CompleteRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getReportCompleteMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CONNECT = 0;
  private static final int METHODID_SEND_SAMPLE = 1;
  private static final int METHODID_REPORT_COMPLETE = 2;

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
        case METHODID_CONNECT:
          serviceImpl.connect((masterworker.Common.ConnectRequest) request,
              (io.grpc.stub.StreamObserver<masterworker.Common.ConnectResponse>) responseObserver);
          break;
        case METHODID_SEND_SAMPLE:
          serviceImpl.sendSample((masterworker.Common.SampleRequest) request,
              (io.grpc.stub.StreamObserver<masterworker.Common.SampleResponse>) responseObserver);
          break;
        case METHODID_REPORT_COMPLETE:
          serviceImpl.reportComplete((masterworker.Common.CompleteRequest) request,
              (io.grpc.stub.StreamObserver<masterworker.Common.CompleteResponse>) responseObserver);
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
          getConnectMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              masterworker.Common.ConnectRequest,
              masterworker.Common.ConnectResponse>(
                service, METHODID_CONNECT)))
        .addMethod(
          getSendSampleMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              masterworker.Common.SampleRequest,
              masterworker.Common.SampleResponse>(
                service, METHODID_SEND_SAMPLE)))
        .addMethod(
          getReportCompleteMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              masterworker.Common.CompleteRequest,
              masterworker.Common.CompleteResponse>(
                service, METHODID_REPORT_COMPLETE)))
        .build();
  }

  private static abstract class MasterServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MasterServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return masterworker.Common.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MasterService");
    }
  }

  private static final class MasterServiceFileDescriptorSupplier
      extends MasterServiceBaseDescriptorSupplier {
    MasterServiceFileDescriptorSupplier() {}
  }

  private static final class MasterServiceMethodDescriptorSupplier
      extends MasterServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    MasterServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (MasterServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MasterServiceFileDescriptorSupplier())
              .addMethod(getConnectMethod())
              .addMethod(getSendSampleMethod())
              .addMethod(getReportCompleteMethod())
              .build();
        }
      }
    }
    return result;
  }
}
