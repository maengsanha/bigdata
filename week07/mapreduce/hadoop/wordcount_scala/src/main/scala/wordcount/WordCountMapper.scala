package wordcount

import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.hadoop.mapreduce.Mapper

class WordCountMapper extends Mapper[Object, Text, Text, IntWritable] {
  private var word = new Text()
  private final val one = new IntWritable(1)

  override def map(key: Object, value: Text, context: Mapper[Object, Text, Text, IntWritable]#Context): Unit = {
    value.toString.split(" ").foreach(token => {
      word.set(token)
      context.write(word, one)
    })
  } // end method map.

} // end class WordCountMapper.
