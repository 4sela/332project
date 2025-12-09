
package Shared

import Worker.MergeThread

import java.io.{BufferedInputStream, BufferedOutputStream, FileInputStream, FileOutputStream}
import java.nio.file.Path
import scala.jdk.CollectionConverters.*

type Key = Array[Byte]
type Value = Array[Byte]

type KeyValueArray = Array[Byte]
type KeyValuePair = (Key, Value)

type DataArray = Array[Byte]
type DataStream = LazyList[DataArray]

type Path = String
type JavaPath = java.nio.file.Path

type MergingStructure = List[LazyList[KeyValueArray]]

object KeyValueArray:
	val SIZE: Int = Key.SIZE + Value.SIZE

object Key:
	val SIZE: Int = 10

object Value:
	val SIZE: Int = 90


object DataStream:
	val NUMBER_OF_KEYVALUE_PER_CHUNK: Int = 1000
	val SIZE_CHUNK: Int = NUMBER_OF_KEYVALUE_PER_CHUNK * KeyValueArray.SIZE



object COMMON_VALUE:
  /**aimed number of key_value poer final files.*/
  val SIZE_REGULAR_FINAL_FILE: Int = 10_000




object MyOrdering extends Ordering[KeyValueArray]:
	override def compare(x: KeyValueArray, y: KeyValueArray): Int = {
    java.util.Arrays.compareUnsigned(x, 0, Key.SIZE, y, 0, Key.SIZE)
  }




extension (key_value: KeyValueArray)
	def getValue: Value = key_value.drop(Key.SIZE)
	def getKey: Key = key_value.take(Key.SIZE)
	def getKeyValuePair: KeyValuePair = key_value.splitAt(Key.SIZE)


extension (key_value: KeyValuePair)
	def toKeyValueArray: KeyValueArray = (key_value._1.toList ::: key_value._2.toList).toArray
	def key: Key = key_value._1
	def value: Value = key_value._2


extension (array: DataArray)
	def sort: DataArray =
		array.sliding(KeyValueArray.SIZE, KeyValueArray.SIZE).toList.sorted(MyOrdering).flatten.toArray
	
	def write(path: Path): Option[Path] =
		IO_OPERATION.write(array, path)


extension (path: Path)
  def getPath: java.nio.file.Path = {
    java.nio.file.Paths.get(path)
  }

  def getPathPlusName(name: String): java.nio.file.Path = {
    java.nio.file.Paths.get(path, name)
  }

  def read(read_full_file: Boolean = true): Option[DataArray | DataStream] = {
    IO_OPERATION.read(path, read_full_file)
  }

  def read_full: Option[DataArray] = {
    IO_OPERATION.read_full(path)
  }

  def write_if_directory(name: String, dataArray: DataArray): Option[Path] = {
    IO_OPERATION.create_file(path, name) match {
      case None => None
      case Some(file_path) =>
        IO_OPERATION.write(dataArray,file_path)
    }
  }

  def print_n_first_keyvalue(n: Int = 25): Unit = {
    path.read_full match {
      case Some(value) =>
        value.sliding(KeyValueArray.SIZE,KeyValueArray.SIZE).map(_.take(Key.SIZE)).take(n).foreach(
          k =>
            k.foreach(str => print((str.toInt & 0xff).toString +  " "))
            println("")
        )
      case None => println("not a file")
    }
  }

  def read_slow: Option[DataStream] ={
    IO_OPERATION.read_slow(path)
  }

  def is_sorted: Option[Boolean] = {
    path.read_full match {
      case Some(data) => {
        val list_data = data.sliding(KeyValueArray.SIZE,KeyValueArray.SIZE).toList
        Some(list_data.zip(list_data.tail).forall((a,b) => MyOrdering.compare(a,b) <= 0))
      }
      case None => None
    }
  }




extension (path: JavaPath)
	def isDirectory: Boolean =
		java.nio.file.Files.isDirectory(path)

	def isNormalFile: Boolean =
		java.nio.file.Files.isRegularFile(path)

	def exist: Boolean =
		java.nio.file.Files.exists(path)


extension (stream: DataStream)
	def slideByKeyValue: LazyList[KeyValueArray] =
		stream.flatMap(_.sliding(KeyValueArray.SIZE, KeyValueArray.SIZE).toList)


extension (inputs: List[LazyList[KeyValueArray]])
  def mergeAllSorted: LazyList[KeyValueArray] = {
    val activeStreams = inputs.filter(_.nonEmpty)
    if (activeStreams.isEmpty) {
      LazyList.empty
    } else {
      val (minStream, index) = activeStreams.zipWithIndex.
        minBy{case (stream, _) => stream.head}(using MyOrdering)
      minStream.head #:: activeStreams.updated(index, minStream.tail).mergeAllSorted
    }
  }




object IO_OPERATION:
  def read(path: Path, read_full_file: Boolean = true): Option[DataArray | DataStream] = {
    if (read_full_file) read_full(path) else read_slow(path)
  }

  def read_full(path: Path): Option[DataArray] = {
    val file = new BufferedInputStream(new FileInputStream(path))
    try {
      val return_value: DataArray = file.readAllBytes()
      Some(return_value)
    } catch {
      case _: Throwable => Option.empty
    } finally {
      file.close()
    }
  }

  def read_slow(path: Path): Option[DataStream] = {
    try {
      val file = new BufferedInputStream(new FileInputStream(path))
      val iterator: Iterator[DataArray] = new Iterator[DataArray] {
        var nextChunk: Option[DataArray] = fetchNext()

        def fetchNext(): Option[DataArray] =
          try {
            val buffer = new Array[Byte](DataStream.SIZE_CHUNK)
            val bytesRead = file.readNBytes(buffer, 0, DataStream.SIZE_CHUNK)

            if (bytesRead > 0) {
              if (bytesRead < DataStream.SIZE_CHUNK) {
                Some(buffer.take(bytesRead))
              }
              else {
                Some(buffer)
              }
            } else {
              close()
              None
            }
          } catch {
            case _: Throwable => {
              close()
              None
            }
          }

        def close(): Unit = file.close()

        override def hasNext: Boolean = nextChunk.isDefined

        override def next(): DataArray =
          val res = nextChunk.get
          nextChunk = fetchNext()
          res
      }

      Some(LazyList.from(iterator))

    } catch {
      case _: Throwable => None
    }
  }

  def write(data: DataArray, path: Path): Option[Path] = {
    val file = new BufferedOutputStream(new FileOutputStream(path))
    try {
      file.write(data)
      Some(path)
    } catch {
      case _: Throwable => Option.empty
    } finally {
      file.close()
    }
  }

  def scan_directory_name(path: Path): Option[List[Path]] = {
    try {
      val full_path = path.getPath
      if (full_path.exist && full_path.isDirectory) {
        Some(
          java.nio.file.Files.list(full_path).map(_.toString).toList.asScala.toList
        )
      } else {
        return None
      }
    } catch {
      case e: Exception =>
        println(s"fail to read the directory. Error : ${e.getMessage}")
        return None
    }
  }
	//how_many_strat_to_explore <0 => explore all of the file system from here
  def print_all_file_name(directory: Path)(
    information_to_print: JavaPath => String = _.toString,
    way_to_print: String => Unit = println,
    how_many_strat_to_explore: Int = 2): Unit = {

		def print_with_javaPath(path: JavaPath)(num_strat_remaining: Int = how_many_strat_to_explore): Unit = {
			if (path.exist && num_strat_remaining!=0)
				way_to_print(information_to_print(path))
				if (path.isDirectory) {
					java.nio.file.Files.list(path).forEach(print_with_javaPath(_)(num_strat_remaining-1))
				}
		}
		try{
			val full_path = directory.getPath
			print_with_javaPath(full_path)()
		}catch {
			case e: java.io.IOException =>
				println(s"error while printing a directory at : \"$directory\" with error : ${e.getMessage}")
		}
	}

  def print_all_file_name(directory: Path): Unit = {
    print_all_file_name(directory)()
  }

  def create_directory(path: Path, name: String): Option[Path] = {
		try {
			val full_path = path.getPathPlusName(name)
			if (java.nio.file.Files.exists(full_path) && java.nio.file.Files.isDirectory(full_path)) {
				println(s"The directory already exist ! at : ${path}")
				Some(full_path.toString)
			} else {
				val new_file = java.nio.file.Files.createDirectory(full_path)
				println(s"File successfully created : ${new_file.getFileName.toString}")
				Some(new_file.toString)
			}
		} catch {
			case _:java.io.IOException =>
				println(s"Fail to create the directory because of : ${path}")
				None
		}
	}

  def create_file(directory: Path, name: String): Option[Path] = {
		try {
			val full_path = directory.getPathPlusName(name)
			if (java.nio.file.Files.exists(full_path)) {
				println(s"The file already exist ! at : ${full_path.toString}")
				Some(full_path.toString)
			} else {
				val new_file = java.nio.file.Files.createFile(full_path)
				println(s"File successfully created : ${new_file.getFileName.toString}")
				Some(new_file.toString)
			}
		} catch {
			case e: java.io.IOException =>
				println(s"Fail to created the file at :  $directory. ${e.getMessage}")
				None
		}
	}

  def delete_single_file(path: Path): Unit = {
		try {
			val full_path = path.getPath
			if (!java.nio.file.Files.exists(full_path)){
				println(s"The file doesn't exist ! (at : ${path})")
			}
			else if (!java.nio.file.Files.isRegularFile(full_path)) {
				println(s"try to delete something not a file; at : $path")
			} else {
				java.nio.file.Files.delete(full_path)
				println(s"File successfully deleted : ${path}")
			}
		} catch {
			case e: java.io.IOException =>
				println(s"Fail to delete the file at :  $path. Because of ${e.getMessage}")
		}
	}

  def delete_single_directory(path: Path): Unit = {
		try {
			val full_path = java.nio.file.Paths.get(path)
			if (!java.nio.file.Files.exists(full_path)){
				println(s"The directory doesn't exist ! (at : ${path})")
			}
			else if (!java.nio.file.Files.isDirectory(full_path)) {
				println(s"try to delete something not a directory; at : $path")
			} else {
				java.nio.file.Files.delete(full_path)
				println(s"directory successfully deleted : ${path}")
			}
		} catch {
			case e: java.io.IOException =>
				println(s"Fail to delete the directory at :  $path. Because of ${e.getMessage}")
		}
	}

  def delete_fully_path(path: Path): Unit = {
		try {
			val full_path = path.getPath
			if (!full_path.exist){
				println(s"fail to delete directory because it doesn't exist ! at $path")
			}
			else if (full_path.isDirectory) {
				val list_of_subfiles = java.nio.file.Files.list(full_path).toList.asScala.toList.map(_.toString)
				list_of_subfiles.foreach(delete_fully_path)
				java.nio.file.Files.delete(full_path)
				println(s"successfully delete the directory $path and all of the sub files")
			}
			else {
				java.nio.file.Files.delete(full_path)
			}
		} catch {
			case e: Exception =>
				println(s"fail to delete directory at $path because of ${e.getMessage}")
		}
	}

	/** ++++++ assume that there is only the inputfile/tempfile in the given directory ++++++ */
  def slow_read_all_file_parallel(directory_path: Path): List[DataStream] = {
    scan_directory_name(directory_path).get.map(read_slow).map(_.getOrElse(LazyList.empty)).filter(_.nonEmpty)
  }

  def get_lazy_sorted_list(directory_path: Path): LazyList[KeyValueArray] = {
    slow_read_all_file_parallel(directory_path).mergeAllSorted
  }

  def slow_read_all_file_in_raw(directory_path: Path): DataStream = {
    LazyList.from(scan_directory_name(directory_path).get).map(read_slow).map(_.getOrElse(LazyList.empty)).flatMap(_.toArray)
  }


  def merge_all_sorted_file(origin_directory: Path, destination_directory: Path): Option[Path] = {
    try {
      val merging_threads = MergeThread(origin_directory,destination_directory)
      merging_threads.run()
      //because merging was implemented as a thread ...
      merging_threads.join()
      Some(destination_directory)
    }catch {
      case e: Throwable => {
        System.err.println(s"the merging operation failed because : \"${e.getMessage} \"")
        None
      }
    }
  }



object k:
  @main
  def main(): Unit =
    print("\n")
    "./output5/partition.4".print_n_first_keyvalue(10)
