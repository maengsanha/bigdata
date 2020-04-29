package wc;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

public class WordCount extends Configured implements Tool {
  public int run(String[] strings) throws Exception {
    Job myJob = Job.getInstance(getConf());
    myJob.setJarByClass(WordCount.class);

    myJob.setMapperClass(WordCountMapper.class);
    myJob.setReducerClass(WordCountReducer.class);
    myJob.setCombinerClass(WordCountReducer.class);

    myJob.setMapOutputKeyClass(Text.class);
    myJob.setMapOutputValueClass(IntWritable.class);

//    myJob.setInputFormatClass(TextInputFormat.class);
//    myJob.setOutputFormatClass(TextOutputFormat.class);

    FileInputFormat.addInputPath(myJob, new Path(strings[0]));
    FileOutputFormat.setOutputPath(myJob, new Path(strings[0] + ".out"));

    myJob.waitForCompletion(true);

    return 0;
  }
}
