package Master

import scala.collection.mutable

/**
 *
 * @param numWorkers
 */
class StateManager(numWorkers: Int, logger: TransactionLogger) {

  // These are Master phases!
  sealed trait MasterState
  case object INIT extends MasterState
  case object SAMPLING extends MasterState
  case object PARTITIONING extends MasterState
  case object MERGING extends MasterState
  case object DONE extends MasterState

  // And these are Worker phases
  sealed trait WorkerState
  case object CONNECTED extends WorkerState
  case object SAMPLED extends WorkerState
  case object SHUFFLED extends WorkerState
  case object MERGED extends WorkerState
  case object CRASHED extends WorkerState

  private var currentState: MasterState = INIT
  private val workerStates = mutable.Map[Int, WorkerState]()

  def updateWorkerState(workerId: Int, state: WorkerState): Unit = {
    workerStates(workerId) = state
    logger.logWorkerEvent(workerId, s"STATE -> $state")
    checkStateTransition()
  }

  def getCurrentMasterState: MasterState = currentState

  // This checks if all workers are in a given state
  // Useful because:
  private def allWorkersInState(target: WorkerState): Boolean = {
    workerStates.size == numWorkers && workerStates.values.forall(_ == target)
  }

  // Here we change Master phases when if conditions are met
  private def checkStateTransition(): Unit = {
    currentState match {
      case INIT =>
        if (workerStates.size == numWorkers) {
          currentState = SAMPLING
          println("Transition: INIT → SAMPLING")
        }

      case SAMPLING =>
        if (allWorkersInState(SAMPLED)) {
          currentState = PARTITIONING
          println("Transition: SAMPLING → PARTITIONING")
        }

      case PARTITIONING =>
        if (allWorkersInState(SHUFFLED)) {
          currentState = MERGING
          println("Transition: PARTITIONING → MERGING")
        }

      case MERGING =>
        if (allWorkersInState(MERGED)) {
          currentState = DONE
          println("Transition: MERGING → DONE")
        }

      case DONE => // we are finished here =D
    }
  }
}