package Master

class StateManager(numWorkers: Int) {
  sealed trait MasterState
  case object INIT extends MasterState
  case object SAMPLING extends MasterState
  case object PARTITIONING extends MasterState
  case object MERGING extends MasterState
  case object DONE extends MasterState

  private var currentState: MasterState = INIT
  private val workerStates = mutable.Map[Int, WorkerState]()

  def updateWorkerState(workerId: Int, state: WorkerState): Unit = {
    workerStates(workerId) = state
    checkStateTransition() // See if we should move to next phase
  }

  private def checkStateTransition(): Unit = {
    currentState match {
      case INIT =>
        if (workerStates.size == numWorkers) {
          currentState = SAMPLING
          // notifyStateChange()
        }

      case SAMPLING =>
        // if (allWorkersInState(WorkerState.SAMPLED)) {
        //   currentState = PARTITIONING
        //   // notifyStateChange()
        // }

      case PARTITIONING =>
        // if (allWorkersInState(WorkerState.SHUFFLED)) {
        //   currentState = MERGING
        //   // notifyStateChange()
        // }

      case MERGING =>
        // if (allWorkersInState(WorkerState.MERGED)) {
        //   currentState = DONE
        //   // notifyStateChange()
      //   }

      case DONE => // finished
    }
  }
}