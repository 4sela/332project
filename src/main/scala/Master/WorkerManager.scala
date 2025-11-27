package Master

/**
 *
 */

class WorkerManager {
  case class WorkerInfo(
                         id: Int,
                         ipAddress: String,
                         port: Int,
                         isAlive: Boolean = true
                       )

  private val workers = mutable.Map[Int, WorkerInfo]()
  private val workerOrder = mutable.ArrayBuffer[Int]() 

  def registerWorker(ipAddress: String, port: Int): Int = {
    val workerId = workers.size + 1
    workers(workerId) = WorkerInfo(workerId, ipAddress, port)
    workerOrder += workerId
    workerId
  }

  def getWorkerOrdering: List[String] = {
    workerOrder.map(id => workers(id).ipAddress).toList
  }

  def markWorkerCrashed(workerId: Int): Unit = {
    workers.get(workerId).foreach { w =>
      workers(workerId) = w.copy(isAlive = false)
    }
  }

  def getAllWorkers: List[WorkerInfo] = workers.values.toList
}
