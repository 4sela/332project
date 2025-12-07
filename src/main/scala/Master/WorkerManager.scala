package Master

import scala.collection.mutable

/**
 * This is responsible for...
 */
class WorkerManager {
  case class WorkerInfo(
                         id: Int,
                         ipAddress: String,
                         port: Int,
                         isAlive: Boolean = true
                       )

  private val workers = mutable.Map[Int, WorkerInfo]()
  private var nextId: Int = 1

  def registerWorker(ipAddress: String, port: Int): Int = {
    val workerId: Int = nextId
    nextId += 1

    workers(workerId) = WorkerInfo(workerId, ipAddress, port)
    println(s"Worker $workerId registered at $ipAddress:$port")

    return workerId
  }

  def markWorkerCrashed(workerId: Int): Unit = {
    workers.get(workerId).foreach { w =>
      workers(workerId) = w.copy(isAlive = false)
      println(s"Worker $workerId marked as crashed")
    }
  }

  def getAllWorkers: List[WorkerInfo] = workers.values.toList

  def getWorker(workerId: Int): Option[WorkerInfo] = workers.get(workerId)

  def getWorkerOrdering: List[Int] = workers.keys.toList.sorted
}
