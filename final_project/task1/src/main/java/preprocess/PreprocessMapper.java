package preprocess;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;


public class PreprocessMapper extends Mapper<Object, Text, IntWritable, IntWritable> {
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
  } // end method map.

} // end class PreprocessMapper.
