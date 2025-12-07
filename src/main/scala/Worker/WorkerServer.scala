package Worker

import io.grpc.stub.StreamObserver
import masterworker.*
import scala.jdk.CollectionConverters.*

/**
 * gRPC service implementation for Worker
 * This receives commands from the Master
 */
class WorkerServiceImpl(controlUnit: ControlUnit) extends WorkerServiceGrpc.WorkerServiceImplBase {

  override def receivePartitions(
                                  request: Common.PartitionsMessage,
                                  responseObserver: StreamObserver[Common.Ack]
                                ): Unit = {
    try {
      val boundaries = request.getBoundariesList.asScala.map(_.toByteArray).toArray
      val myPartition = controlUnit.workerId - 1  // Worker 1→0, Worker 2→1, Worker 3→2

      controlUnit.receivePartitionBoundaries(boundaries, myPartition)

      val reply = Common.Ack.newBuilder()
        .setSuccess(true)
        .build()

      responseObserver.onNext(reply)
      responseObserver.onCompleted()

    } catch {
      case e: Exception =>
        println(s"Error receiving partitions: ${e.getMessage}")
        e.printStackTrace()

        val reply = Common.Ack.newBuilder()
          .setSuccess(false)
          .build()

        responseObserver.onNext(reply)
        responseObserver.onCompleted()
    }
  }

  override def startPhase(
                           request: Common.PhaseCommand,
                           responseObserver: StreamObserver[Common.Ack]
                         ): Unit = {
    try {
      val phase = request.getPhase
      println(s"Received START PHASE command: $phase")

      phase match {
        case "PARTITIONING" =>
          // Run in separate thread to avoid blocking gRPC
          new Thread(() => controlUnit.startPartitioning()).start()

        case "MERGING" =>
          // Run in separate thread to avoid blocking gRPC
          new Thread(() => controlUnit.startMerging()).start()

        case _ =>
          println(s"Unknown phase: $phase")
      }

      val reply = Common.Ack.newBuilder()
        .setSuccess(true)
        .build()

      responseObserver.onNext(reply)
      responseObserver.onCompleted()

    } catch {
      case e: Exception =>
        println(s"Error starting phase: ${e.getMessage}")
        e.printStackTrace()

        val reply = Common.Ack.newBuilder()
          .setSuccess(false)
          .build()

        responseObserver.onNext(reply)
        responseObserver.onCompleted()
    }
  }
}