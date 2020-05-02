package wordcount

import java.lang

import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.hadoop.mapreduce.Reducer

class WordCountReducer extends Reducer[Text, IntWritable, Text, IntWritable] {
  override def reduce(key: Text, values: lang.Iterable[IntWritable], context: Reducer[Text, IntWritable, Text, IntWritable]#Context): Unit = {
    // TODO
  }
}
