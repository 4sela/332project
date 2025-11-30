package Shared

import java.io.{BufferedInputStream, FileInputStream}


type Key = Array[Byte]
type Value = Array[Byte]

type KeyValueArray = Array[Byte]
type KeyValuePair = (Key, Value)

type DataArray = Array[Byte]
type DataStream = LazyList[DataArray]

type Path = String



object KeyValueArray:
	val SIZE: Int = Key.SIZE + Value.SIZE
	
object Key:
	val SIZE: Int = 10
	
object Value:
	val SIZE: Int = 90
	
	
	
	
extension (key_value: KeyValueArray)
	def getValue: Value = key_value.drop(Key.SIZE)
	def getKey: Key = key_value.take(Key.SIZE)
	def getKeyValuePair: KeyValuePair= key_value.splitAt(Key.SIZE)
	

	
	
extension (key_value: KeyValuePair)
	def toKeyValueArray: KeyValueArray = (key_value._1.toList ::: key_value._2.toList).toArray
	def key: Key = key_value._1
	def value: Value = key_value._2
	
	
	
	
given Ordering[KeyValueArray]:
	override def compare(x: KeyValueArray, y: KeyValueArray)(implicit order: Ordering[Key]): Int =
		x.getKey.zip(y.getKey).find(_ != _).map((x, y) => if (x > y) 1 else -1).getOrElse(0)
		
		
given Ordering[KeyValuePair]:
	override def compare(x: (Key, Value), y: (Key, Value))(implicit order: Ordering[Key]): Int =
		order.compare(x.key,y.key)
		


extension (array: DataArray)
	def sort: DataArray =
		array.sliding(KeyValueArray.SIZE, KeyValueArray.SIZE).toList.sorted.flatten.toArray
	
	def write(path: Path): Option[Path] =
		IO_OPERATION.write(array,path)
	
	
	
extension (path: Path)
	def read(read_full_file: Boolean = true): Option[DataArray|DataStream] =
		IO_OPERATION.read(path, read_full_file)
	
	def read_full: Option[DataArray] =
		IO_OPERATION.read_full(path)

	def read_slow: Option[DataStream] =
		IO_OPERATION.read_slow(path)
	
	
extension (stream: DataStream)
	def slideByKeyValue: LazyList[KeyValueArray] =
		stream.flatMap(_.sliding(KeyValueArray.SIZE, KeyValueArray.SIZE).toList)
		
	
	
	
	
object IO_OPERATION:
	def read(path: Path, read_full_file: Boolean = true): Option[DataArray|DataStream] =
		if (read_full_file) read_full(path) else read_slow(path)
	
	def read_full(path: Path): Option[DataArray]  =
		val file = new BufferedInputStream(new FileInputStream(path))
		try {
			val return_value: DataArray = file.readAllBytes()
			Some(return_value)
		}catch {
			case _: Throwable => Option.empty
		}finally {
			file.close()
		}
		
	
	//TODO
	def read_slow(path: Path): Option[DataStream] = ???
	
	//TODO
	def write(data: DataArray, path: Path): Option[Path] = ???	
	
  //TODO 
	def scan_directory_name(path: Path): List[String] = ??? 
	
	//TODO
	def create_file(directory: Path, name: String): Option[Path] = ???

	//TODO
	def delete_single_temp_file(path: Path): Unit = ???
	
	//TODO 
	def delete_all_temp_directory(path: Path): Unit = ??? 
	
	
	/**++++++ assume that there is only the inputfile/tempfile in the given directory ++++++*/
	def slow_read_all_file_parallel(directory_path: Path): List[DataStream] =
		scan_directory_name(directory_path).map(read_slow).map(_.getOrElse(LazyList.empty)).filter(_.nonEmpty)
		
	
  //TODO check if it works properly !!! 
	def slow_read_all_file_in_raw(directory_path: Path): DataStream =  
	  (LazyList.from(scan_directory_name(directory_path))).map(read_slow).map(_.getOrElse(LazyList.empty)).flatMap(_.toArray)
	

	