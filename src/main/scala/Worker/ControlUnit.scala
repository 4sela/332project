package Worker
import Shared.* 

class ControlUnit {
	def sendMessage(message: NetworkMessage): Unit = ???
	
	def sampling(): DataArray = ???
	
	def createSplittingThread(): SplittingThread = ???
	def createMergingThread(): MergeThread = ???
	
	def createWorkersServerManager(): WorkerServerManager = ???
	def createMasterServer(): MasterServer = ???
	
	def createInputMemoryData(): Unit = ???
	def createTemporaryMemoryData(): Unit = ???
	def createOutputMemoryData(): Unit = ???
	
}
