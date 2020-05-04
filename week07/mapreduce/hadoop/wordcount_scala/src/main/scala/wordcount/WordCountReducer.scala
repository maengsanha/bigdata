package wordcount

import java.lang

import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.hadoop.mapreduce.Reducer

import collection.JavaConverters._

class WordCountReducer extends Reducer[Text, IntWritable, Text, IntWritable] {
  val count = new IntWritable()

  override def reduce(key: Text, values: lang.Iterable[IntWritable], context: Reducer[Text, IntWritable, Text, IntWritable]#Context): Unit = {
    count.set(values.asScala.map(x => x.get()).sum)
    context.write(key, count)
  } // end method reduce.

} // end class WordCountReducer
