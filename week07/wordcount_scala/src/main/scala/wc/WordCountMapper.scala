package wc

import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.hadoop.mapreduce.Mapper


class WordCountMapper extends Mapper[Object, Text, Text, IntWritable] {
  override def map(key: Object, value: Text, context: Mapper[Object, Text, Text, IntWritable]#Context): Unit = {

  }
}
