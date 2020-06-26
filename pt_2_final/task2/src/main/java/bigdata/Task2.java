package bigdata;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.StringTokenizer;

public class Task2 extends Configured implements Tool {

  public static void main(String[] args) throws Exception {
    ToolRunner.run(new Task2(), args);
  } //  end main.

  public int run(String[] strings) throws Exception {
    Job job = Job.getInstance(getConf());
    job.setJarByClass(Task2.class);

    job.setMapperClass(DegreeMapper.class);
    job.setReducerClass(DegreeReducer.class);
    job.setCombinerClass(DegreeReducer.class);

    job.setMapOutputKeyClass(IntWritable.class);
    job.setMapOutputValueClass(IntWritable.class);

    job.setOutputKeyClass(IntWritable.class);
    job.setOutputValueClass(IntWritable.class);

    FileInputFormat.addInputPath(job, new Path(strings[0]));
    FileOutputFormat.setOutputPath(job, new Path(strings[1]));

    job.waitForCompletion(true);

    return 0;
  } //  end run.

  public static class DegreeMapper extends Mapper<Object, Text, IntWritable, IntWritable> {
    IntWritable tmp = new IntWritable();
    final IntWritable one = new IntWritable(1);

    // map emits (u, 1), (v, 1) with given edge (u, v).
    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      StringTokenizer tokenizer = new StringTokenizer(value.toString());

      while (tokenizer.hasMoreTokens()) {
        tmp.set(Integer.parseInt(tokenizer.nextToken()));
        context.write(tmp, one);
      }
    } //  end map.

  } //  end DegreeMapper.

  public static class DegreeReducer extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {

    // reduce emits the degree of node u.
    @Override
    protected void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable v: values) sum += v.get();
      context.write(key, new IntWritable(sum));
    } //  end reduce.

  } //  end DegreeReducer.

} //  end Task2.
