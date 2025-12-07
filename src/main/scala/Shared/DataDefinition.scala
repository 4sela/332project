package Shared


type Byte = Char
type DataArray = Array[Byte]
type DataStream = LazyList[DataArray]
type DataName = String 
type Path = java.nio.file.Path

/*
sealed trait DataBlock:
def toDataBlockArray: DataBlockArray
def toDataBlockStream: DataBlockStream
def toDataBlockOnDisc: DataBlockOnDisc


case class DataBlockOnDisc(name: DataName, path: Path) extends DataBlock:
override def toDataOnDisc: DataBlockOnDisc = this

override def toDataBlockArray: DataBlockArray = ???

override def toDataBlockStream: DataBlockStream = ???


case class DataBlockArray(name: DataName, dataArray: DataArray) extends DataBlock:
override def toDataBlockStream: DataBlockStream = ???

override def toDataBlockArray: DataBlockArray = this

override def toDataBlockOnDisc: DataBlockOnDisc = ???

def sortArray: Unit = ???


case class DataBlockStream(name: DataName, dataStream: DataStream) extends DataBlock:
override def toDataBlockOnDisc: DataBlockOnDisc = ???

override def toDataBlockArray: DataBlockArray = ???

override def toDataBlockStream: DataBlockStream = ???


object MemoryOperation:
def readFullDirectory(): DataStream = ???
def readFileAsStream(path: Path): Option[DataStream] = ???
def readFileAsArray(path: Path): Option[DataArray] = ???




*/