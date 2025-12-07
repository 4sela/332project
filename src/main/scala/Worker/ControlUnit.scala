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

	private var assignedPartition: Int = -1

	// Worker's own gRPC server for receiving commands from Master
	private var workerServer: Server = _
	private val workerPort: Int = findAvailablePort()

	// Channel to communicate with Master
	private var masterChannel: ManagedChannel = _
	private var masterStub: MasterServiceGrpc.MasterServiceBlockingStub = _

	// Worker ID assigned by Master
	var workerId: Int = -1

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

		println(s"Successfully connected! Assigned Worker ID: $workerId")
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
								println(s"     Loaded file: $file (${data.length} bytes)")
								Some(data)
							case None =>
								println(s"     Failed to load file: $file")
								None
						}
					}
				case None =>
					println(s"   Failed to scan directory: $dir")
					List.empty
			}
		}

		val totalBytes = inputData.map(_.length).sum
		println(s"Total input data loaded: $totalBytes bytes (${inputData.size} files)")
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
			println(" Samples sent successfully!")
		} else {
			println(" Failed to send samples!")
		}
	}

	// Called by WorkerServiceImpl when Master sends partition boundaries
	// (via common.proto ReceivePartitions RPC)
	def receivePartitionBoundaries(boundaries: Array[Array[Byte]], myPartition: Int): Unit = {
		println(s"Received ${boundaries.length} partition boundaries from Master")
		boundaries.zipWithIndex.foreach { case (b, i) =>
			println(s"  Boundary[$i]: ${b.map("%02X".format(_)).mkString(" ")}")
		}
		println(s"Assigned to handle partition: $myPartition")
		partitionBoundaries = boundaries
		assignedPartition = myPartition
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
			if (inputData.isEmpty) {
				println(s"[WARN] Worker $workerId: inputData is empty => no records to partition")
				// ensure we still have partition map with empty partitions
				val numPartitions = math.max(1, partitionBoundaries.length + 1)
				sortedPartitions = (0 until numPartitions).map(i => i -> Array.empty[Byte]).toMap
			} else {
				// safer combine (avoid reduce on empty)
				val allData = inputData.reduce(_ ++ _)
				val sortedData = allData.sort

				println(s"  Total sorted bytes: ${sortedData.length}")
				// show first/last keys (small debugging)
				val recSize = KeyValueArray.SIZE
				if (sortedData.length >= recSize) {
					val firstKey = sortedData.slice(0, Key.SIZE)
					val lastKey = sortedData.slice(sortedData.length - recSize, sortedData.length - recSize + Key.SIZE)
					println(s"  First key hex: ${firstKey.map("%02X".format(_)).mkString(" ")}")
					println(s"  Last  key hex: ${lastKey.map("%02X".format(_)).mkString(" ")}")
				}

				println("  Partitioning data by boundaries...")
				// Partition the sorted data based on boundaries
				sortedPartitions = partitionData(sortedData)
			}

			println(s"Partitioning complete! Created ${sortedPartitions.size} partitions")
			sortedPartitions.toSeq.sortBy(_._1).foreach { case (partId, data) =>
				println(s"    Partition $partId: ${data.length} bytes (${data.length / KeyValueArray.SIZE} records)")
			}

			// Report completion to Master (using common.proto ReportComplete)
			reportPhaseComplete("PARTITIONING")

		} catch {
			case e: Exception =>
				println(s"Error during partitioning: ${e.getMessage}")
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
			// ONLY write OUR assigned partition
			sortedPartitions.get(assignedPartition) match {
				case Some(data) =>
					val outputFile = s"$outputDir/partition.$assignedPartition"
					data.write(outputFile) match {
						case Some(path) =>
							println(s" Written partition $assignedPartition to $outputFile (${data.length} bytes)")
						case None =>
							println(s" Failed to write partition $assignedPartition")
					}
				case None =>
					println(s" No data for partition $assignedPartition (empty partition)")
					// Still create an empty file
					val outputFile = s"$outputDir/partition.$assignedPartition"
					Array.empty[Byte].write(outputFile)
			}

			println(s"Merging complete!")
			reportPhaseComplete("MERGING")

			println("\n" + "="*60)
			println("WORKER COMPLETED SUCCESSFULLY!")
			println("="*60 + "\n")

			shutdown()

		} catch {
			case e: Exception =>
				println(s"Error during merging: ${e.getMessage}")
				e.printStackTrace()
		}
	}

	private def partitionData(sortedData: DataArray): Map[Int, DataArray] = {
		val recSize = KeyValueArray.SIZE
		val records = sortedData.sliding(recSize, recSize).toArray

		val numPartitions = partitionBoundaries.length + 1
		// initialize empty partitions (each as Array[Byte])
		val initial = (0 until numPartitions).map(i => i -> scala.collection.mutable.ArrayBuffer.empty[Byte]).toMap

		// fill partitions
		val filled = records.foldLeft(initial) { (acc, record) =>
			val key = record.take(Key.SIZE)
			val part = findPartitionIndex(key)
			// guard partition bounds: if part is out of range, clamp
			val partClamped = math.max(0, math.min(numPartitions - 1, part))
			acc(partClamped) ++= record
			acc
		}

		// convert mutable buffers back to plain arrays (DataArray)
		filled.map { case (k, buf) => k -> buf.toArray }
	}


	private def findPartitionIndex(key: Key): Int = {
		// if no boundaries, everything goes to partition 0
		if (partitionBoundaries.isEmpty) return 0

		var lo = 0
		var hi = partitionBoundaries.length - 1
		var ans = partitionBoundaries.length // default => last partition

		while (lo <= hi) {
			val mid = (lo + hi) >>> 1
			val cmp = MyOrdering.compare(key, partitionBoundaries(mid))
			if (cmp < 0) {
				// key < boundary[mid] -> candidate for partition mid
				ans = mid
				hi = mid - 1
			} else {
				// key >= boundary[mid] -> go right
				lo = mid + 1
			}
		}
		// ans is index of first boundary greater than key -> partition = ans
		ans
	}


	private def compareKeys(a: Array[Byte], b: Array[Byte]): Int = {
		val len = math.min(a.length, math.min(b.length, Key.SIZE))
		for (i <- 0 until len) {
			val diff = (a(i).toInt & 0xFF) - (b(i).toInt & 0xFF)
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
			println(s"Reported $phase completion to Master")
		} else {
			println(s"Failed to report $phase completion")
		}
	}

	private def shutdown(): Unit = {
		println("Shutting down Worker...")
		masterChannel.shutdown()
		workerServer.shutdown()
	}
}