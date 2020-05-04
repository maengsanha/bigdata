package wordcount

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.util.ToolRunner

object WordCountTest {

  def main(not_used: Array[String]): Unit = {
    val args = Array("src/test/resources/the_little_prince.txt")

    val conf = new Configuration()
    conf.setInt("mapreduce.job.reduces", 5)

    ToolRunner.run(conf, new WordCount, args)
  } // end method main.

} // end class WordCountTest.
