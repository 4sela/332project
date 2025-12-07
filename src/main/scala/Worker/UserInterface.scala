package Worker

import io.grpc.ManagedChannelBuilder
import masterworker.{Common, MasterServiceGrpc}
import java.net.InetAddress

object UserInterface {
	def main(args: Array[String]): Unit = {
		// Parse command line arguments
		if (args.length < 4) {
			println("Usage: worker <master IP:port> -I <input dirs...> -O <output dir>")
			println("Example: worker 192.168.1.100:30040 -I /data1/input /data2/input -O /home/worker/output")
			sys.exit(1)
		}

		val masterAddress = args(0)
		val (masterIp, masterPort) = parseMasterAddress(masterAddress)

		val inputDirs = parseInputDirectories(args)
		val outputDir = parseOutputDirectory(args)

		if (inputDirs.isEmpty) {
			println("Error: No input directories specified")
			sys.exit(1)
		}

		if (outputDir.isEmpty) {
			println("Error: No output directory specified")
			sys.exit(1)
		}

		println("="*60)
		println("  DISTRIBUTED SORTING - WORKER NODE")
		println("="*60)
		println(s"  Master: $masterIp:$masterPort")
		println(s"  Input directories: ${inputDirs.mkString(", ")}")
		println(s"  Output directory: ${outputDir.get}")
		println("="*60)
		println()

		// Create and start Worker
		val controlUnit = new ControlUnit(
			masterIp = masterIp,
			masterPort = masterPort,
			inputDirs = inputDirs,
			outputDir = outputDir.get
		)

		controlUnit.start()
	}

	private def parseMasterAddress(address: String): (String, Int) = {
		val parts = address.split(":")
		if (parts.length != 2) {
			println(s"Error: Invalid master address format: $address")
			sys.exit(1)
		}
		(parts(0), parts(1).toInt)
	}

	private def parseInputDirectories(args: Array[String]): List[String] = {
		val iIndex = args.indexOf("-I")
		if (iIndex == -1) return List.empty

		val oIndex = args.indexOf("-O")
		val endIndex = if (oIndex == -1) args.length else oIndex

		args.slice(iIndex + 1, endIndex).toList
	}

	private def parseOutputDirectory(args: Array[String]): Option[String] = {
		val oIndex = args.indexOf("-O")
		if (oIndex == -1 || oIndex + 1 >= args.length) return None

		Some(args(oIndex + 1))
	}
}