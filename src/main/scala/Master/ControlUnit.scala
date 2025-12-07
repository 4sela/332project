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
    // Start the gRPC server
    server = ServerBuilder.forPort(port)
      .addService(new MasterServiceImpl(this))
      .build()
    server.start()

    val localIp = java.net.InetAddress.getLocalHost.getHostAddress
    println(s"Master server started on $localIp:$port")
    logger.logSystemEvent(s"Master started on $localIp:$port")

    // Run the orchestration in a separate thread so server can handle requests
    val orchestrationThread = new Thread(() => runOrchestration())
    orchestrationThread.start()

    // Keep server running
    server.awaitTermination()
  }

  private def runOrchestration(): Unit = {
    println(s"\nWaiting for $numWorkers workers to connect...\n")

    // Phase 1: Wait for all workers to connect
    waitForState(SAMPLING)
    println("All workers connected!\n")

    // Phase 2: Workers automatically send samples (handled by MasterServiceImpl)
    println("Waiting for all workers to send samples...\n")
    waitForState(PARTITIONING)
    println("All samples received!\n")

    // Phase 3: Calculate boundaries and send to workers
    val boundaries = samplingCoordinator.calculatePartitions()
    println(s"Calculated ${boundaries.length} partition boundaries")
    logger.logSystemEvent("Partition boundaries calculated")

    println("Sending partition boundaries to all workers...")
    sendBoundariesToAllWorkers(boundaries)

    // Give workers a moment to receive boundaries
    Thread.sleep(500)

    // Phase 4: Tell workers to start partitioning
    println("Broadcasting: START PARTITIONING\n")
    broadcastStartPhase("PARTITIONING")

    println("Waiting for all workers to finish partitioning...\n")
    waitForState(MERGING)
    println("All workers finished partitioning!\n")

    // Phase 5: Tell workers to start merging
    println("Broadcasting: START MERGING\n")
    broadcastStartPhase("MERGING")

    println("Waiting for all workers to finish merging...\n")
    waitForState(DONE)

    // Phase 6: Print final results
    println("\n" + "=" * 50)
    println("DISTRIBUTED SORTING COMPLETE!")
    println("=" * 50)
    println("\nWorker Ordering:")
    workerManager.getWorkerOrdering.foreach { id =>
      workerManager.getWorker(id).foreach { w =>
        println(s"Worker $id: ${w.ipAddress}:${w.port}")
      }
    }
    println("\n" + "=" * 50 + "\n")

    logger.logSystemEvent("Sorting complete - all phases finished")

    // Shutdown server
    println("Shutting down Master server...")
    server.shutdown()
  }

  // Helper: Wait until StateManager reaches a specific state
  private def waitForState(targetState: MasterState): Unit = {
    while (stateManager.getCurrentMasterState != targetState) {
      Thread.sleep(100) // Check every 100ms
    }
  }

  // Helper: Send boundaries to all workers
  private def sendBoundariesToAllWorkers(boundaries: Array[Array[Byte]]): Unit = {
    workerManager.getAllWorkers.foreach { worker =>
      sendBoundariesToWorker(worker, boundaries)
    }
  }

  // Helper: Send boundaries to a specific worker
  private def sendBoundariesToWorker(worker: workerManager.WorkerInfo, boundaries: Array[Array[Byte]]): Unit = {
    try {
      // Create gRPC channel to worker
      val channel = io.grpc.ManagedChannelBuilder
        .forAddress(worker.ipAddress, worker.port)
        .usePlaintext()
        .build()

      val stub = masterworker.WorkerServiceGrpc.newBlockingStub(channel)

      // Convert boundaries to protobuf format
      import com.google.protobuf.ByteString
      import scala.jdk.CollectionConverters._

      val boundariesProto = boundaries.map(b => ByteString.copyFrom(b)).toSeq.asJava

      val request = masterworker.Common.PartitionsMessage.newBuilder()
        .addAllBoundaries(boundariesProto)
        .build()

      val response = stub.receivePartitions(request)

      if (response.getSuccess) {
        println(s"Worker ${worker.id} received boundaries")
        logger.logWorkerEvent(worker.id, "Received partition boundaries")
      } else {
        println(s"Worker ${worker.id} failed to receive boundaries")
      }

      channel.shutdown()
    } catch {
      case e: Exception =>
        println(s"Error sending boundaries to Worker ${worker.id}: ${e.getMessage}")
        logger.logWorkerEvent(worker.id, s"Error sending boundaries: ${e.getMessage}")
    }
  }

  // Helper: Tell all workers to start a phase
  private def broadcastStartPhase(phase: String): Unit = {
    workerManager.getAllWorkers.foreach { worker =>
      sendStartPhaseToWorker(worker, phase)
    }
  }

  // Helper: Tell a specific worker to start a phase
  private def sendStartPhaseToWorker(worker: workerManager.WorkerInfo, phase: String): Unit = {
    try {
      val channel = io.grpc.ManagedChannelBuilder
        .forAddress(worker.ipAddress, worker.port)
        .usePlaintext()
        .build()

      val stub = masterworker.WorkerServiceGrpc.newBlockingStub(channel)

      val request = masterworker.Common.PhaseCommand.newBuilder()
        .setPhase(phase)
        .build()

      val response = stub.startPhase(request)

      if (response.getSuccess) {
        println(s"Worker ${worker.id} starting $phase")
        logger.logWorkerEvent(worker.id, s"Started $phase phase")
      } else {
        println(s"Worker ${worker.id} failed to start $phase")
      }

      channel.shutdown()
    } catch {
      case e: Exception =>
        println(s"Error sending START $phase to Worker ${worker.id}: ${e.getMessage}")
        logger.logWorkerEvent(worker.id, s"Error starting $phase: ${e.getMessage}")
    }
  }

  def onSampleReceived(workerId: Int, samples: Seq[Array[Byte]]): Unit = {
    samplingCoordinator.addSample(workerId, samples)
    stateManager.updateWorkerState(workerId, SAMPLED)
    logger.logWorkerEvent(workerId, s"SAMPLED (${samples.size} keys)")

    if (samplingCoordinator.isComplete) {
      val boundaries = samplingCoordinator.calculatePartitions()
      println("Partition boundaries calculated: " +
        boundaries.map(_.mkString("[", ",", "]")).mkString(", "))

      workerManager.getAllWorkers.foreach { w =>
        println(s"Would send boundaries to Worker ${w.id} at ${w.ipAddress}:${w.port}")
        // TODO: send boundaries via gRPC
      }

      println("Transition: SAMPLING -> PARTITIONING")
    }
  }

  def onWorkerConnected(workerId: Int): Unit = {
    stateManager.updateWorkerState(workerId, CONNECTED)
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

  /**
   *
   * @param workerId
   */
  def onPartitioningComplete(workerId: Int): Unit = {
    stateManager.updateWorkerState(workerId, SHUFFLED)
    logger.logWorkerEvent(workerId, "PARTITIONING complete")

    // Check if all workers finished partitioning
    if (stateManager.getCurrentMasterState == MERGING) {
      println("All workers finished PARTITIONING!")
      // TODO: This will trigger merging phase (we'll add this in next step)
    }
  }

  /**
   *
   * @param workerId
   */
  def onMergingComplete(workerId: Int): Unit = {
    stateManager.updateWorkerState(workerId, MERGED)
    logger.logWorkerEvent(workerId, "MERGING complete")

    // Check if all workers finished merging
    if (stateManager.getCurrentMasterState == DONE) {
      println("All workers finished MERGING!")
      println("\n=== SORTING COMPLETE ===")
      println("Worker ordering:")
      workerManager.getWorkerOrdering.foreach(id => {
        workerManager.getWorker(id).foreach(w => println(s"  Worker $id: ${w.ipAddress}:${w.port}"))
      })
      println("========================\n")
    }
  }
}