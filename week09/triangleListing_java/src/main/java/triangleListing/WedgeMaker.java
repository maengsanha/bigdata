package triangleListing;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class WedgeMaker extends Configured implements Tool {

  @Override
  public int run(String[] strings) throws Exception {
    Job job = Job.getInstance(getConf());
    job.setJarByClass(WedgeMaker.class);

    job.setMapperClass(WedgeMapper.class);
    job.setReducerClass(WedgeReducer.class);

    job.setMapOutputKeyClass(IntWritable.class);
    job.setMapOutputValueClass(IntWritable.class);

    job.setOutputKeyClass(IntPairWritable.class);
    job.setOutputValueClass(IntWritable.class);

    job.setOutputFormatClass(SequenceFileOutputFormat.class);

    FileInputFormat.addInputPath(job, new Path(strings[0]));
    FileOutputFormat.setOutputPath(job, new Path(strings[1]));

    job.waitForCompletion(true);

    return 0;
  } // end method run.

  public static class WedgeMapper extends Mapper<Object, Text, IntWritable, IntWritable> {
    IntWritable u = new IntWritable();
    IntWritable v = new IntWritable();

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      StringTokenizer tokenizer = new StringTokenizer(value.toString());
      u.set(Integer.parseInt(tokenizer.nextToken()));
      v.set(Integer.parseInt(tokenizer.nextToken()));
      if (u.get() < v.get()) context.write(u, v);
      else context.write(v, u);
    } // end method map.

  } // end class WedgeMapper.

  public static class WedgeReducer extends Reducer<IntWritable, IntWritable, IntPairWritable, IntWritable> {
    IntPairWritable pair = new IntPairWritable();

    @Override
    protected void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
      List<Integer> neighbors = new ArrayList<>();

      for (IntWritable value: values)
        neighbors.add(value.get());

      for (int i=0; i<neighbors.size(); ++i)
        for (int j=0; j<neighbors.size(); ++j)
          if (neighbors.get(i) < neighbors.get(j)) {
            pair.set(neighbors.get(i), neighbors.get(j));
            context.write(pair, key);
          }
    } // end method reduce.

  } // end class WedgeReducer.

} // end class WedgeMaker.
