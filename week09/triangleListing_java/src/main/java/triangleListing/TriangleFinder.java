package triangleListing;

import com.sun.org.apache.xml.internal.serializer.OutputPropertyUtils;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class TriangleFinder extends Configured implements Tool {

  @Override
  public int run(String[] strings) throws Exception {
    Job job = Job.getInstance(getConf());
    job.setJarByClass(TriangleFinder.class);

    job.setReducerClass(TriangleReducer.class);

    job.setMapOutputKeyClass(IntPairWritable.class);
    job.setMapOutputValueClass(IntWritable.class);

    job.setOutputKeyClass(IntPairWritable.class);
    job.setOutputValueClass(IntWritable.class);

    job.setOutputFormatClass(TextOutputFormat.class);

    MultipleInputs.addInputPath(job, new Path(strings[1]), SequenceFileInputFormat.class, WedgeMapper.class);
    MultipleInputs.addInputPath(job, new Path(strings[0]), TextInputFormat.class, EdgeMapper.class);
    FileOutputFormat.setOutputPath(job, new Path(strings[2]));

    job.waitForCompletion(true);

    return 0;
  } // end method run.

  public static class EdgeMapper extends Mapper<Object, Text, IntPairWritable, IntWritable> {
    IntPairWritable pair = new IntPairWritable();
    IntWritable minusOne = new IntWritable(-1);

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      StringTokenizer tokenizer = new StringTokenizer(value.toString());

      int u = Integer.parseInt(tokenizer.nextToken());
      int v = Integer.parseInt(tokenizer.nextToken());

      if (u < v) pair.set(u, v);
      else pair.set(v, u);

      context.write(pair, minusOne);
    } // end method map.

  } // end class EdgeMapper.

  public static class WedgeMapper extends Mapper<IntPairWritable, IntWritable, IntPairWritable, IntWritable> {

    @Override
    protected void map(IntPairWritable key, IntWritable value, Context context) throws IOException, InterruptedException {
      context.write(key, value);
    } // end method map.

  } // end class WedgeMapper.

  public static class TriangleReducer extends Reducer<IntPairWritable, IntWritable, IntPairWritable, IntWritable> {
    IntWritable v = new IntWritable();

    @Override
    protected void reduce(IntPairWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
      List<Integer> nodes = new ArrayList<>();
      boolean check = false;

      for (IntWritable value: values) {
        if (value.get() == -1) check = true;
        else nodes.add(value.get());
      }

      if (check)
        for (int node: nodes) {
          v.set(node);
          context.write(key, v);
        }
    } // end method reduce.

  } // end class TriangleReducer.

} // end class TriangleFinder.
