import io.grpc.{Server, ServerBuilder}
import io.grpc.stub.StreamObserver
import masterworker.{Common, MasterServiceGrpc}

class MasterServiceImpl extends MasterServiceGrpc.MasterServiceImplBase {
  override def connect(req: Common.ConnectRequest,
                       responseObserver: StreamObserver[Common.ConnectResponse]): Unit = {
    val reply = Common.ConnectResponse.newBuilder()
      .setWorkerId(1) // assign a dummy ID for now
      .build()
    responseObserver.onNext(reply)
    responseObserver.onCompleted()
  }

  override def sendSample(req: Common.SampleRequest,
                          responseObserver: StreamObserver[Common.SampleResponse]): Unit = {
    val reply = Common.SampleResponse.newBuilder().setSuccess(true).build()
    responseObserver.onNext(reply)
    responseObserver.onCompleted()
  }

  override def reportComplete(req: Common.CompleteRequest,
                              responseObserver: StreamObserver[Common.CompleteResponse]): Unit = {
    val reply = Common.CompleteResponse.newBuilder().setSuccess(true).build()
    responseObserver.onNext(reply)
    responseObserver.onCompleted()
  }
}

object MasterServer {
  def main(args: Array[String]): Unit = {
    val server: Server = ServerBuilder.forPort(30040)
      .addService(new MasterServiceImpl)
      .build()
    server.start()
    println("Master server started on port 30040")
    server.awaitTermination()
  }
}
