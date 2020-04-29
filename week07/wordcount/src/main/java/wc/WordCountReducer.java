package wc;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.Text;

import java.io.IOException;

public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
  IntWritable cnt = new IntWritable();

  @Override
  protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
    int sum = 0;

    for (IntWritable value : values)
      sum += value.get();

    cnt.set(sum);
    context.write(key, cnt);
  }
}
