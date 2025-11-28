package Master

import java.io.{File, FileWriter, PrintWriter}

/**
 * 
 */
class TransactionLogger {
  private val logFile = new File("master_transactions.log")
  private val writer = new PrintWriter(new FileWriter(logFile, true))

  private def log(message: String): Unit = {
    val timestamp = System.currentTimeMillis()
    writer.println(s"$timestamp: $message")
    writer.flush()
  }

  def logStateChange(oldState: String, newState: String): Unit = {
    log(s"STATE CHANGE: $oldState -> $newState")
  }

  def logWorkerEvent(workerId: Int, event: String): Unit = {
    log(s"WORKER $workerId: $event")
  }

  def logSystemEvent(event: String): Unit = {
    log(s"SYSTEM: $event")
  }
}