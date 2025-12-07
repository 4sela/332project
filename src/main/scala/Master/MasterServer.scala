package Master

import Master.{SamplingCoordinator, StateManager, WorkerManager}
import io.grpc.{Server, ServerBuilder}
import io.grpc.stub.StreamObserver
import masterworker.{Common, MasterServiceGrpc}
import scala.jdk.CollectionConverters._

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

    val scalaSamples = sampleList.asScala.map(_.toByteArray).toSeq

    controlUnit.onSampleReceived(workerId, scalaSamples)

    val reply = Common.SampleResponse.newBuilder().setSuccess(true).build()
    responseObserver.onNext(reply)
    responseObserver.onCompleted()
  }

  override def reportComplete(req: Common.CompleteRequest,
                              responseObserver: StreamObserver[Common.CompleteResponse]): Unit = {
    val workerId = req.getWorkerId
    val phase = req.getPhase

    println(s"Worker $workerId reported completion of phase: $phase")

    // Handle different phases
    phase match {
      case "PARTITIONING" =>
        controlUnit.onPartitioningComplete(workerId)

      case "MERGING" =>
        controlUnit.onMergingComplete(workerId)

      case _ =>
        println(s"WARNING: Unknown phase '$phase' reported by Worker $workerId")
    }

    val reply = Common.CompleteResponse.newBuilder().setSuccess(true).build()
    responseObserver.onNext(reply)
    responseObserver.onCompleted()
  }
}

/**
 * REMOVED - Use UserInterface.main() instead
 */
// object MasterServer {
//   def main(args: Array[String]): Unit = {
//     val control = new Master.ControlUnit(numWorkers = 1, port = 30040)
//     control.start()
//   }
// }
