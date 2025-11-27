package Master

/**
 *
 */
/*
class NetworkManager(controlUnit: ControlUnit) {
  private var server: Server = _

  def start(): (String, Int) = {
    val port = findAvailablePort() // Don't hard-code!

    server = ServerBuilder
      .forPort(port)
      .addService(new MasterServiceImpl(controlUnit))
      .build()
      .start()

    val ip = InetAddress.getLocalHost.getHostAddress
    (ip, port)
  }

  def sendToWorker(workerId: Int, message: MasterCommand): Unit = {
    // Use gRPC client to send message to specific worker
  }

  def broadcastToAllWorkers(message: MasterCommand): Unit = {
    // Send to all workers
  }
}
*/
