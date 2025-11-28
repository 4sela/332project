package Master

import io.grpc.{Server, ServerBuilder}

/**
 *
 * @param numWorkers
 * @param port
 */
class ControlUnit(numWorkers: Int, port: Int = 30040) {
  val workerManager     = new WorkerManager()
  val samplingCoordinator = new SamplingCoordinator(numWorkers)
  val logger = new TransactionLogger()
  val stateManager = new StateManager(numWorkers, logger)

  private var server: Server = _

  def start(): Unit = {
    server = ServerBuilder.forPort(port)
      .addService(new MasterServiceImpl(this))
      .build()
    server.start()
    println(s"Master server started on port $port")

    // Things to consider:
    // 1. Then request samples from workers
    // 2. And calculate partitions
    // 3. Then coordinate partitioning phase
    // 4. Coordinate merging phase
    // 5. Print worker ordering and exit status

    server.awaitTermination()
  }

  def onSampleReceived(workerId: Int, samples: Seq[Array[Byte]]): Unit = {
    samplingCoordinator.addSample(workerId, samples)
    stateManager.updateWorkerState(workerId, stateManager.SAMPLED)
    logger.logWorkerEvent(workerId, s"SAMPLED (${samples.size} keys)")

    if (samplingCoordinator.isComplete) {
      val boundaries = samplingCoordinator.calculatePartitions()
      println("Partition boundaries calculated: " +
        boundaries.map(_.mkString("[", ",", "]")).mkString(", "))

      workerManager.getAllWorkers.foreach { w =>
        println(s"Would send boundaries to Worker ${w.id} at ${w.ipAddress}:${w.port}")
        // TODO: send boundaries via gRPC
      }

      println("Transition: SAMPLING → PARTITIONING")
    }
  }

  def onWorkerConnected(workerId: Int): Unit = {
    stateManager.updateWorkerState(workerId, stateManager.CONNECTED)
    logger.logWorkerEvent(workerId, "CONNECTED")
  }

  // This is an example hook for future phases
  def onAllSamplesReceived(): Unit = {
    val boundaries = samplingCoordinator.calculatePartitions()
    println("Partition boundaries calculated: " +
      boundaries.map(_.mkString("[", ",", "]")).mkString(", "))

    workerManager.getAllWorkers.foreach { w =>
      println(s"Would send boundaries to Worker ${w.id} at ${w.ipAddress}:${w.port}")
      // TODO: push boundaries via WorkerService or client-side stubs
    }
  }
}