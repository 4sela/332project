package Master

import scala.collection.mutable

/**
 *
 * @param numWorkers Number of workers defined by the user of the command-line-interface
 */
class SamplingCoordinator(numWorkers: Int) {

  // This is to store all samples received from the workers
  private val samples = mutable.ArrayBuffer[Array[Byte]]()
  private var samplesReceived = 0

  /** Add samples from a worker */
  def addSample(workerId: Int, sampleData: Seq[Array[Byte]]): Unit = {
    samples ++= sampleData
    samplesReceived += 1
    println(s"Received ${sampleData.size} samples from Worker $workerId")
  }

  /** This is to check if all workers have sent their samples */
  def isComplete: Boolean = samplesReceived == numWorkers

  /** This calculates partition boundaries once all samples are collected */
  def calculatePartitions(): Array[Array[Byte]] = {
    require(isComplete, "Not all workers have sent samples yet")

    val sortedSamples = samples.sortWith(compareKeys)
    val boundaries = Array.ofDim[Byte](numWorkers - 1, 10) // assume 10‑byte keys
    val step = sortedSamples.length / numWorkers

    // Here we loop and
    for (i <- 0 until numWorkers - 1) {
      boundaries(i) = sortedSamples((i + 1) * step)
    }

    println("Calculated partition boundaries")
    boundaries
  }

  private def compareKeys(a: Array[Byte], b: Array[Byte]): Boolean = {
    for (i <- 0 until 10) {
      if (a(i) != b(i)) {
        return (a(i) & 0xFF) < (b(i) & 0xFF)
      }
    }
    false
  }
}
