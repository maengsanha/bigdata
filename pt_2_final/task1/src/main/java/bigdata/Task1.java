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
import java.util.HashSet;
import java.util.StringTokenizer;

public class Task1 extends Configured implements Tool {

  public static void main(String[] args) throws Exception {
    ToolRunner.run(new Task1(), args);
  } //  end main.

  public int run(String[] strings) throws Exception {
    Job job = Job.getInstance(getConf());
    job.setJarByClass(Task1.class);

    job.setMapperClass(SelfLoopRemover.class);
    job.setReducerClass(DuplicateRemover.class);
    job.setCombinerClass(DuplicateRemover.class);

    job.setMapOutputKeyClass(IntWritable.class);
    job.setMapOutputValueClass(IntWritable.class);

    job.setOutputKeyClass(IntWritable.class);
    job.setOutputValueClass(IntWritable.class);

    FileInputFormat.addInputPath(job, new Path(strings[0]));
    FileOutputFormat.setOutputPath(job, new Path(strings[1]));

    job.waitForCompletion(true);

    return 0;
  } //  end run.

  public static class SelfLoopRemover extends Mapper<Object, Text, IntWritable, IntWritable> {
    private IntWritable ou = new IntWritable();
    private IntWritable ov = new IntWritable();

    // map removes self-loop and emits in order of value.
    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      StringTokenizer tokenizer = new StringTokenizer(value.toString());

      int u = Integer.parseInt(tokenizer.nextToken());
      int v = Integer.parseInt(tokenizer.nextToken());

      if (u != v) { // removes duplicates.
        if (u < v) { ou.set(u); ov.set(v); }
        else { ou.set(v); ov.set(u); }
        context.write(ou, ov);
      }
    } //  end map.

  } //  end SelfLoopRemover.

  public static class DuplicateRemover extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {

    // reduce removes duplicates.
    @Override
    protected void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
      HashSet<Integer> nodes = new HashSet<>();

      for (IntWritable value: values)
        if (!nodes.contains(value.get())) {
          nodes.add(value.get());
          context.write(key, value);
        }
    } //  end reduce.

  } //  end DuplicateRemover.

} //  end Task1.
