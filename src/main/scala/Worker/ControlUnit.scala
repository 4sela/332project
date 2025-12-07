package Worker

import Shared.*
import io.grpc.{ManagedChannel, ManagedChannelBuilder, Server, ServerBuilder}
import masterworker.{Common, MasterServiceGrpc, WorkerServiceGrpc}
import com.google.protobuf.ByteString
import scala.jdk.CollectionConverters.*
import java.net.InetAddress

class ControlUnit( masterIp: String,
									 masterPort: Int,
									 inputDirs: List[String],
									 outputDir: String ) {

	// Worker's own gRPC server for receiving commands from Master
	private var workerServer: Server = _
	private val workerPort: Int = findAvailablePort()

	// Channel to communicate with Master
	private var masterChannel: ManagedChannel = _
	private var masterStub: MasterServiceGrpc.MasterServiceBlockingStub = _

	// Worker ID assigned by Master
	private var workerId: Int = -1

	// Partition boundaries received from Master
	private var partitionBoundaries: Array[Array[Byte]] = Array.empty

	// Data management
	private var inputData: List[DataArray] = List.empty
	private var sortedPartitions: Map[Int, DataArray] = Map.empty

	def start(): Unit = {
		println("Starting Worker...")

		// Step 1: Start Worker's own gRPC server to receive commands from Master
		startWorkerServer()

		// Step 2: Connect to Master
		connectToMaster()

		// Step 3: Load input data
		loadInputData()

		// Step 4: Send samples to Master
		sendSamples()

		// Worker server keeps running, waiting for Master's commands
		println("Worker is ready and waiting for Master's commands...")
		workerServer.awaitTermination()
	}

	private def findAvailablePort(): Int = {
		// Find an available port dynamically
		val serverSocket = new java.net.ServerSocket(0)
		val port = serverSocket.getLocalPort
		serverSocket.close()
		port
	}

	private def startWorkerServer(): Unit = {
		workerServer = ServerBuilder.forPort(workerPort)
			.addService(new WorkerServiceImpl(this))
			.build()
		workerServer.start()

		val localIp = InetAddress.getLocalHost.getHostAddress
		println(s"Worker server started on $localIp:$workerPort")
	}

	private def connectToMaster(): Unit = {
		println(s"Connecting to Master at $masterIp:$masterPort...")

		// Create gRPC channel to Master
		masterChannel = ManagedChannelBuilder
			.forAddress(masterIp, masterPort)
			.usePlaintext()
			.build()

		masterStub = MasterServiceGrpc.newBlockingStub(masterChannel)

		// Send connection request (using common.proto ConnectRequest)
		val localIp = InetAddress.getLocalHost.getHostAddress
		val request = Common.ConnectRequest.newBuilder()
			.setIp(localIp)
			.setPort(workerPort)
			.build()

		val response = masterStub.connect(request)
		workerId = response.getWorkerId

		println(s"✓ Successfully connected! Assigned Worker ID: $workerId")
	}

	private def loadInputData(): Unit = {
		println("Loading input data...")

		inputData = inputDirs.flatMap { dir =>
			println(s"  Reading from directory: $dir")
			IO_OPERATION.scan_directory_name(dir) match {
				case Some(files) =>
					files.flatMap { file =>
						IO_OPERATION.read_full(file) match {
							case Some(data) =>
								println(s"    ✓ Loaded file: $file (${data.length} bytes)")
								Some(data)
							case None =>
								println(s"    ✗ Failed to load file: $file")
								None
						}
					}
				case None =>
					println(s"  ✗ Failed to scan directory: $dir")
					List.empty
			}
		}

		val totalBytes = inputData.map(_.length).sum
		println(s"✓ Total input data loaded: $totalBytes bytes (${inputData.size} files)")
	}

	private def sendSamples(): Unit = {
		println("Generating and sending samples to Master...")

		// Collect samples from input data (sample every N-th record)
		val sampleRate = 1000 // Sample 1 out of every 1000 records
		val samples = inputData.flatMap { dataArray =>
			dataArray.sliding(KeyValueArray.SIZE, KeyValueArray.SIZE)
				.zipWithIndex
				.filter { case (_, index) => index % sampleRate == 0 }
				.map { case (record, _) => record.take(Key.SIZE) } // Only send the key
				.toList
		}

		println(s"  Collected ${samples.size} samples")

		// Convert to protobuf format
		val samplesProto = samples.map(s => ByteString.copyFrom(s)).asJava

		// Send using common.proto SampleRequest
		val request = Common.SampleRequest.newBuilder()
			.setWorkerId(workerId)
			.addAllSamples(samplesProto)
			.build()

		val response = masterStub.sendSample(request)

		if (response.getSuccess) {
			println("✓ Samples sent successfully!")
		} else {
			println("✗ Failed to send samples!")
		}
	}

	// Called by WorkerServiceImpl when Master sends partition boundaries
	// (via common.proto ReceivePartitions RPC)
	def receivePartitionBoundaries(boundaries: Array[Array[Byte]]): Unit = {
		println(s"✓ Received ${boundaries.length} partition boundaries from Master")
		partitionBoundaries = boundaries
	}

	// Called by WorkerServiceImpl when Master tells us to start partitioning
	// (via common.proto StartPhase RPC)
	def startPartitioning(): Unit = {
		println("\n" + "="*60)
		println("Starting PARTITIONING phase...")
		println("="*60)

		try {
			// Sort and partition all input data
			println("  Sorting all input data...")
			val allData = inputData.reduce(_ ++ _)
			val sortedData = allData.sort

			println("  Partitioning data by boundaries...")
			// Partition the sorted data based on boundaries
			sortedPartitions = partitionData(sortedData)

			println(s"✓ Partitioning complete! Created ${sortedPartitions.size} partitions")
			sortedPartitions.foreach { case (partId, data) =>
				println(s"    Partition $partId: ${data.length} bytes")
			}

			// Report completion to Master (using common.proto ReportComplete)
			reportPhaseComplete("PARTITIONING")

		} catch {
			case e: Exception =>
				println(s"✗ Error during partitioning: ${e.getMessage}")
				e.printStackTrace()
		}
	}

	// Called by WorkerServiceImpl when Master tells us to start merging
	// (via common.proto StartPhase RPC)
	def startMerging(): Unit = {
		println("\n" + "="*60)
		println("Starting MERGING phase...")
		println("="*60)

		try {
			// Write partitions to output files
			sortedPartitions.toList.sortBy(_._1).foreach { case (partId, data) =>
				val outputFile = s"$outputDir/partition.$partId"
				data.write(outputFile) match {
					case Some(path) =>
						println(s"  ✓ Written partition $partId to $outputFile (${data.length} bytes)")
					case None =>
						println(s"  ✗ Failed to write partition $partId")
				}
			}

			println(s"✓ Merging complete! Written ${sortedPartitions.size} output files")

			// Report completion to Master (using common.proto ReportComplete)
			reportPhaseComplete("MERGING")

			println("\n" + "="*60)
			println("WORKER COMPLETED SUCCESSFULLY!")
			println("="*60 + "\n")

			// Shutdown
			shutdown()

		} catch {
			case e: Exception =>
				println(s"✗ Error during merging: ${e.getMessage}")
				e.printStackTrace()
		}
	}

	private def partitionData(sortedData: DataArray): Map[Int, DataArray] = {
		val records = sortedData.sliding(KeyValueArray.SIZE, KeyValueArray.SIZE).toArray

		// Determine which partition each record belongs to
		val partitionedRecords = records.groupBy { record =>
			val key = record.take(Key.SIZE)
			findPartitionIndex(key)
		}

		// Convert back to DataArray format
		partitionedRecords.map { case (partId, records) =>
			partId -> records.flatten
		}
	}

	private def findPartitionIndex(key: Key): Int = {
		// Binary search to find which partition this key belongs to
		var index = 0
		while (index < partitionBoundaries.length && compareKeys(key, partitionBoundaries(index)) >= 0) {
			index += 1
		}
		index
	}

	private def compareKeys(a: Array[Byte], b: Array[Byte]): Int = {
		for (i <- 0 until 10) {
			val diff = (a(i) & 0xFF) - (b(i) & 0xFF)
			if (diff != 0) return diff
		}
		0
	}

	// Send completion report using common.proto CompleteRequest
	private def reportPhaseComplete(phase: String): Unit = {
		val request = Common.CompleteRequest.newBuilder()
			.setWorkerId(workerId)
			.setPhase(phase)
			.build()

		val response = masterStub.reportComplete(request)

		if (response.getSuccess) {
			println(s"✓ Reported $phase completion to Master")
		} else {
			println(s"✗ Failed to report $phase completion")
		}
	}

	private def shutdown(): Unit = {
		println("Shutting down Worker...")
		masterChannel.shutdown()
		workerServer.shutdown()
	}
}