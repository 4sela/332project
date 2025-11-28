package Worker

import io.grpc.ManagedChannelBuilder
import masterworker.{Common, MasterServiceGrpc}

/**
 * THIS IS A TEMPORARY SCRIPT, ITS JUST FOR TESTING BECAUSE
 * I WANT TO CHECK IF WORKERS CAN ENTER THE SERVER
 *
 * It works!!
 *
 * Lets keep it for now in case we want to do more testing
 */

object TestWorkerClient {
  def main(args: Array[String]): Unit = {
    val channel = ManagedChannelBuilder.forAddress("localhost", 30040)
      .usePlaintext()
      .build()

    val stub = MasterServiceGrpc.newBlockingStub(channel)

    val request = Common.ConnectRequest.newBuilder()
      .setIp("127.0.0.1")
      .setPort(1234)
      .build()

    val response = stub.connect(request)
    println(s"Connected, got workerId = ${response.getWorkerId}")

    channel.shutdown()
  }
}
