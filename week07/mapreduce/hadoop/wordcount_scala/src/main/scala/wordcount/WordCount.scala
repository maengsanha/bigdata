package wordcount

import org.apache.hadoop.conf.Configured
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.util.Tool

class WordCount extends Configured with Tool {

  override def run(args: Array[String]): Unit = {
    val job = Job.getInstance(getConf)
    job.setJarByClass(this.getClass)

    job.setMapperClass(classOf[WordCountMapper])
    job.setReducerClass(classOf[WordCountReducer])
    job.setCombinerClass(classOf[WordCountReducer])

    job.setMapOutputKeyClass(classOf[Text])
    job.setMapOutputValueClass(classOf[IntWritable])

    FileInputFormat.addInputPath(job, new Path(args[0]))
    FileOutputFormat.setOutputPath(job, new Path(args[0] + ".out"))

    job.waitForCompletion(true)
  } // end method run.

} // end class WordCount
