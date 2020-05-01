package wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class WordCountMapper extends Mapper<Object, Text, Text, IntWritable> {

  private Text word = new Text();
  private IntWritable one = new IntWritable(1);

  @Override
  protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
    StringTokenizer tokenizer = new StringTokenizer( value.toString() );
    while ( tokenizer.hasMoreTokens() ) {
      word.set( tokenizer.nextToken().replaceAll("\\W", "") );
      context.write(word, one);
    }
  } // end method map.

} // end class WordCountMapper.
