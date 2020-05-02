package wordcount

import java.util.StringTokenizer

import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.hadoop.mapreduce.Mapper

class WordCountMapper extends Mapper[Object, Text, Text, IntWritable] {

  val word = new Text()
  val one = new IntWritable(1)

  override def map(key: Object, value: Text, context: Mapper[Object, Text, Text, IntWritable]#Context): Unit = {
    val tokenizer = new StringTokenizer(value.toString)
    while (tokenizer.hasMoreTokens) {
      word.set(tokenizer.nextToken().replaceAll("\\W", ""))
      context.write(word, one)
    }
  } // end method map.

} // end class WordCountMapper.
