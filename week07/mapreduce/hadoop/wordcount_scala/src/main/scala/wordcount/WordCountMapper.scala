package wordcount

import java.util.StringTokenizer

import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.hadoop.mapreduce.Mapper

class WordCountMapper extends Mapper[Object, Text, Text, IntWritable] {

  private var word = new Text()
  private val one = new IntWritable(1)

  override def map(key: Object, value: Text, context: Mapper[Object, Text, Text, IntWritable]#Context): Unit = {
    // TODO
  } // end method map.

} // end class WordCountMapper.
