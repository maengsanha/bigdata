package preprocess;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashSet;

public class PreprocessReducer extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {

  // reduce removes duplicates and emits.
  @Override
  protected void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
    HashSet<Integer> nodes = new HashSet<>();

    for (IntWritable value: values)
      if (!nodes.contains(value.get())) {
        nodes.add(value.get());
        context.write(key, value);
      }
  } // end method reduce.

} // end class PreprocessReducer.
