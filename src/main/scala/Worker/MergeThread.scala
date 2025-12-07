package Worker

import Shared.*


case class MergeThread(temporary_directory: Path, final_directory: Path = "./final_directory") extends Thread{
	override def run(): Unit = {
    var lazy_sorted_list = IO_OPERATION.get_lazy_sorted_list(temporary_directory)
    var index: Int = 0
    while (lazy_sorted_list.nonEmpty){
      val (chunks,tail) = lazy_sorted_list.splitAt(COMMON_VALUE.SIZE_REGULAR_FINAL_FILE)
      val data_array = chunks.toList.flatten.toArray
      val final_files = final_directory.write_if_directory("final_file_"+index.toString, data_array)

      if (final_files.isEmpty) System.err.println(s"the file number $index failed to be written in the merging threads at $final_directory!")
      
      index += 1
      lazy_sorted_list = tail
    }
  }
}
