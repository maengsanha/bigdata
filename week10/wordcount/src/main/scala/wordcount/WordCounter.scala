package wordcount

import org.apache.spark.{SparkConf, SparkContext}

object WordCounter {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("WordCount")
    val sc = new SparkContext(conf)

    val input = args(0)
    val output = args(1)

    sc.textFile(input)
      .flatMap(_.split(" "))
      .map(_.replaceAll("\\W", ""))
      .map(s => (s, 1))
      .reduceByKey(_+_)
      .map(x => x._1 + "\t" + x._2)
      .saveAsTextFile(output)
  }
}
