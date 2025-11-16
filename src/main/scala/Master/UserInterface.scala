package Master

enum OutputMessage:
	case StartProcess 
	case WorkerJoin
	case UpdateStates
	case ReportError
	case CrashReport
	case EndSuccessFully


object UserInterface {
	
	def main(args : Array[String]): Unit =
		args.foreach(println)
	
	def printOutputMessage(message: OutputMessage): Unit = ???
}
