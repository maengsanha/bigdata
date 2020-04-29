package wc;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class WordCountMapper extends Mapper<Object, Text, Text, IntWritable> {
  Text word = new Text();
  IntWritable one = new IntWritable(1);

  @Override
  protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
    StringTokenizer st = new StringTokenizer(value.toString());

    while (st.hasMoreTokens()) {
      word.set(st.nextToken().replaceAll("\\W", ""));
      context.write(word, one);
    }
  }
}
