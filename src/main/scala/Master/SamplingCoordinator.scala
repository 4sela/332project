package Master
/*
class SamplingCoordinator(numWorkers: Int) {
  private val samples = mutable.ArrayBuffer[Array[Byte]]() // 10-byte keys
  private var samplesReceived = 0

  def addSample(workerId: Int, sampleData: Array[Array[Byte]]): Unit = {
    samples ++= sampleData
    samplesReceived += 1
  }

  def isComplete: Boolean = samplesReceived == numWorkers

  def calculatePartitions(): Array[Array[Byte]] = {
    val sortedSamples = samples.sortWith(compareKeys)

    val boundaries = Array.ofDim[Byte](numWorkers - 1, 10)
    val step = sortedSamples.length / numWorkers

    for (i <- 0 until numWorkers - 1) {
      boundaries(i) = sortedSamples((i + 1) * step)
    }

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

 */