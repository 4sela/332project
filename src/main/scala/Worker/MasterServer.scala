package Worker

import Shared.NetworkMessage
import NetworkMessage.* 


class MasterServer(controlUnit: ControlUnit) {
	def sendMessage(): Unit = ???
	
	def receiveMessage(message: NetworkMessage): Unit =
		message match {
			case NetworkMessage.JoinProcess => ???
			case NetworkMessage.YouCanJoin => ???
			case NetworkMessage.ImpossibleToJoin => ???
			case NetworkMessage.SendSample => ???
			case NetworkMessage.StartSplitting => ???
			case NetworkMessage.SendingDataFromTo => ???
			case NetworkMessage.ReceiveDataFromTo => ???
			case NetworkMessage.SendingDataFromToAcknowledgment => ???
			case NetworkMessage.ReceiveDataFromToAcknowledgment => ???
			case NetworkMessage.EndSplitting => ???
			case NetworkMessage.EndReceiving => ???
			case NetworkMessage.StartMerging => ???
			case NetworkMessage.EndMerging => ???
			case NetworkMessage.ReportErrorFromWorker => ???
			case NetworkMessage.ReportCrashFromWorker => ???
		}
}
