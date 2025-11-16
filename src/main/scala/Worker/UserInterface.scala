package Worker

enum OutputMessage:
	case StartProcess
	case UpdateNews
	case ReportError
	case CrashReport
	case EndSuccessFully


object UserInterface {
	
	def main(args : Array[String]): Unit =
		args.foreach(println)
	
	def printOutputMessage(message: OutputMessage): Unit = ???
}
