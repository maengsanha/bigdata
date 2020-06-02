package preprocess;


import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Preprocessor extends Configured implements Tool {

  public static void main(String[] args) throws Exception {
    ToolRunner.run(new Preprocessor(), args);
  }

  public int run(String[] strings) throws Exception {
    Job job = Job.getInstance(getConf());
    job.setJarByClass(Preprocessor.class);

    job.setMapperClass(PreprocessMapper.class);
    job.setReducerClass(PreprocessReducer.class);
    job.setCombinerClass(PreprocessReducer.class);

    job.setMapOutputKeyClass(IntWritable.class);
    job.setMapOutputValueClass(IntWritable.class);

    job.setOutputKeyClass(IntWritable.class);
    job.setOutputValueClass(IntWritable.class);

    FileInputFormat.addInputPath(job, new Path(strings[0]));
    FileOutputFormat.setOutputPath(job, new Path(strings[1]));

    job.waitForCompletion(true);

    return 0;
  } // end method run.

} // end class Preprocessor.
