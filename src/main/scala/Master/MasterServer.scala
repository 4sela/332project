package Master

import Master.{SamplingCoordinator, StateManager, WorkerManager}
import io.grpc.{Server, ServerBuilder}
import io.grpc.stub.StreamObserver
import masterworker.{Common, MasterServiceGrpc}

/**
 * 
 * @param controlUnit
 */
class MasterServiceImpl(controlUnit: ControlUnit) extends MasterServiceGrpc.MasterServiceImplBase {
  override def connect(req: Common.ConnectRequest,
                       responseObserver: StreamObserver[Common.ConnectResponse]): Unit = {

    // First lets register the worker
    val workerId = controlUnit.workerManager.registerWorker(req.getIp, req.getPort)
    controlUnit.onWorkerConnected(workerId)

    // Second, lets build and send the response back to the Worker
    val reply = Common.ConnectResponse.newBuilder()
      .setWorkerId(workerId)
      .build()

    responseObserver.onNext(reply)
    responseObserver.onCompleted()
  }

  override def sendSample(req: Common.SampleRequest,
                          responseObserver: StreamObserver[Common.SampleResponse]): Unit = {
    val workerId = req.getWorkerId
    val sampleList = req.getSamplesList
    val scalaSamples = sampleList.toArray.map(_.asInstanceOf[Array[Byte]]).toSeq

    controlUnit.onSampleReceived(workerId, scalaSamples)

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

/**
 * 
 */
object MasterServer {
  def main(args: Array[String]): Unit = {
    val control = new Master.ControlUnit(numWorkers = 1, port = 30040) // This is temporary, the number should be determined in UserInterface likely to 20 or something
    control.start()
  }
}
