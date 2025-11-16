package Worker

import Shared.*

case class WrittingThread(dataArray: DataArray) extends Thread{
	override def run() = ???
	
	private def write(): Option[Path] = ???
	private def acknowledgement(): Unit = ??? 
}
