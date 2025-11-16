package Shared

enum NetworkMessage {
	case JoinProcess
	case YouCanJoin
	case ImpossibleToJoin
	
	case SendSample
	
	case StartSplitting
	
	case SendingDataFromTo
	case ReceiveDataFromTo
	
	case SendingDataFromToAcknowledgment
	case ReceiveDataFromToAcknowledgment
	
	case EndSplitting 
	case EndReceiving 
	
	case StartMerging 
	case EndMerging
	
	case ReportErrorFromWorker
	case ReportCrashFromWorker
}