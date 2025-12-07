package Worker
import Shared.* 
import Master.*

class ControlUnit {
	def sendMessage(message: OutputMessage): Unit = ???
	
	def sampling(): DataArray = ???
	
	def createSplittingThread(): SplittingThread = ???
	def createMergingThread(): MergeThread = ???
	
	def createWorkersServerManager(): WorkerServerManager = ???
	def createMasterServer(): MasterServer = ???
	
	def createInputMemoryData(): Unit = ???
	def createTemporaryMemoryData(): Unit = ???
	def createOutputMemoryData(): Unit = ???
	
}
