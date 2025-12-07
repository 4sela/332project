package Master

enum OutputMessage:
	case StartProcess
	case WorkerJoin
	case UpdateStates
	case ReportError
	case CrashReport
	case EndSuccessFully

/**
 * 
 */
object UserInterface {
	def main(args: Array[String]): Unit = {
		// Parse command line arguments
		if (args.length != 1) {
			println("Usage: master <# of workers>")
			println("Example: master 20")
			sys.exit(1)
		}

		val numWorkers = args(0).toInt

		// Validate input here 
		if (numWorkers <= 0) {
			println("Error: Number of workers must be positive")
			sys.exit(1)
		}

		println("="*60)
		println("  DISTRIBUTED SORTING - MASTER NODE")
		println("="*60)
		println(s"  Expected workers: $numWorkers")
		println(s"  Port: 30040")
		println("="*60)
		println()

		// Create and start Master
		val controlUnit = new ControlUnit(numWorkers, port = 30040)
		controlUnit.start()
	}
}