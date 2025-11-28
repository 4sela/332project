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
		if (args.length != 1) {
			println("Usage: master <# of workers>")
			sys.exit(1)
		}

		val numWorkers = args(0).toInt
		println(s"Starting Master with $numWorkers workers expected...")

		val controlUnit = new ControlUnit(numWorkers)
		controlUnit.start()
	}
}