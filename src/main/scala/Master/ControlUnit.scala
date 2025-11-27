package Master

class ControlUnit(numWorkers: Int) {
  val stateManager = new StateManager(numWorkers)
  val workerManager = new WorkerManager()
  val sampler = new SamplingCoordinator(numWorkers)
  val networkManager = new NetworkManager(this)
  val logger = new TransactionLogger()
 
  def start(): Unit = { 
    // 1. We start the network server here!
    val (ip, port) = networkManager.start()
    println(s"Master listening on $ip:$port")
 
    // 2. And then we wait for workers to connect
    stateManager.waitForState(State.SAMPLING)
 
    // 3. Then request samples from workers
    // 4. And calculate partitions
    // 5. Then coordinate partitioning phase
    // 6. Coordinate merging phase
    // 7. Print worker ordering and exit status
  }
 
  // Should be called by NetworkManager when the messages arrive
  def handleWorkerMessage(workerId: Int, message: WorkerMessage): Unit = {
    message match {
      case ConnectRequest => // ...
      case SampleData => // ...
      case PhaseComplete => // ...
    }
  }
}
