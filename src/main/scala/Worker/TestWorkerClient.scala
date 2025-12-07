package Worker

import io.grpc.ManagedChannelBuilder
import masterworker.{Common, MasterServiceGrpc}
import com.google.protobuf.ByteString
import scala.util.Random

/**
 * THIS IS A TEMPORARY SCRIPT, ITS JUST FOR TESTING BECAUSE
 * I WANT TO CHECK IF WORKERS CAN ENTER THE SERVER
 *
 * It works!!
 *
 * Lets keep it for now in case we want to do more testing
 */
/*
object TestWorkerClient {
  def main(args: Array[String]): Unit = {
    println("=== Test Worker Client Starting ===\n")

    val channel = ManagedChannelBuilder.forAddress("localhost", 30040)
      .usePlaintext()
      .build()

    val stub = MasterServiceGrpc.newBlockingStub(channel)

    println("Step 1: Connecting to Master...")
    val connectRequest = Common.ConnectRequest.newBuilder()
      .setIp("127.0.0.1")
      .setPort(8000 + Random.nextInt(1000)) // Random port for...
      .build()

    val connectResponse = stub.connect(connectRequest)
    val myWorkerId = connectResponse.getWorkerId
    println(s"Connected! Got workerId = $myWorkerId\n")

    // Step 2: Send sample data
    println("Step 2: Sending sample data...")
    val numSamples = 1000
    val samples = generateRandomSamples(numSamples)

    val sampleRequest = Common.SampleRequest.newBuilder()
      .setWorkerId(myWorkerId)
      .addAllSamples(samples)
      .build()

    val sampleResponse = stub.sendSample(sampleRequest)
    println(s"Sent $numSamples samples. Success: ${sampleResponse.getSuccess}\n")

    // Give Master time to process
    Thread.sleep(2000)

    // Step 3: Report PARTITIONING complete
    println("Step 3: Simulating PARTITIONING phase...")
    Thread.sleep(1000)

    val partitioningRequest = Common.CompleteRequest.newBuilder()
      .setWorkerId(myWorkerId)
      .setPhase("PARTITIONING")
      .build()

    val partitioningResponse = stub.reportComplete(partitioningRequest)
    println(s"Reported PARTITIONING complete. Success: ${partitioningResponse.getSuccess}\n")

    Thread.sleep(1000)

    // Step 4: Report MERGING complete
    println("Step 4: Simulating MERGING phase...")
    Thread.sleep(1000)

    val mergingRequest = Common.CompleteRequest.newBuilder()
      .setWorkerId(myWorkerId)
      .setPhase("MERGING")
      .build()

    val mergingResponse = stub.reportComplete(mergingRequest)
    println(s"Reported MERGING complete. Success: ${mergingResponse.getSuccess}\n")

    println("=== Test Worker Client Finished ===")
    channel.shutdown()
  }

  // Generate random 10-byte keys, we do this to test them as samples
  private def generateRandomSamples(count: Int): java.util.List[ByteString] = {
    import scala.jdk.CollectionConverters._

    (1 to count).map { _ =>
      val key = new Array[Byte](10)
      Random.nextBytes(key)
      ByteString.copyFrom(key)
    }.asJava
  }
}*/