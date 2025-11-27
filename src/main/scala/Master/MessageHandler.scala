package Master

/*
class MessageHandler(
                      controlUnit: ControlUnit,
                      stateManager: StateManager,
                      workerManager: WorkerManager,
                      sampler: Sampler,
                      logger: TransactionLogger
                    ) {
  def handleMessage(workerId: Int, message: WorkerMessage): Unit = {
    logger.log(s"Received from Worker $workerId: $message")

    message match {
      case ConnectRequest(ip, port) =>
        val newWorkerId = workerManager.registerWorker(ip, port)
        stateManager.updateWorkerState(newWorkerId, WorkerState.CONNECTED)

      case SampleData(samples) =>
        sampler.addSample(workerId, samples)
        stateManager.updateWorkerState(workerId, WorkerState.SAMPLED)

      case PartitioningComplete =>
        stateManager.updateWorkerState(workerId, WorkerState.SHUFFLED)

      case MergingComplete =>
        stateManager.updateWorkerState(workerId, WorkerState.MERGED)

      case WorkerCrashed =>
        workerManager.markWorkerCrashed(workerId)
    }
  }
}

 */