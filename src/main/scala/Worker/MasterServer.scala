package Worker

import Master.*

class MasterServer(controlUnit: ControlUnit) {
	def sendMessage(): Unit = ???
	
	def receiveMessage(message: OutputMessage): Unit =
		message match {
			case OutputMessage.JoinProcess => ???
			case OutputMessage.YouCanJoin => ???
			case OutputMessage.ImpossibleToJoin => ???
			case OutputMessage.SendSample => ???
			case OutputMessage.StartSplitting => ???
			case OutputMessage.SendingDataFromTo => ???
			case OutputMessage.ReceiveDataFromTo => ???
			case OutputMessage.SendingDataFromToAcknowledgment => ???
			case OutputMessage.ReceiveDataFromToAcknowledgment => ???
			case OutputMessage.EndSplitting => ???
			case OutputMessage.EndReceiving => ???
			case OutputMessage.StartMerging => ???
			case OutputMessage.EndMerging => ???
			case OutputMessage.ReportErrorFromWorker => ???
			case OutputMessage.ReportCrashFromWorker => ???
		}
}
