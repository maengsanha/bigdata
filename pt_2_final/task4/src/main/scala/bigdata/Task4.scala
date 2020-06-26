package bigdata

import org.apache.spark.{SparkConf, SparkContext}

object Task4 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Task4")
    val sc = new SparkContext(conf)

    // make degree data to RDDs.
    val degrees = sc.textFile( args(0) )
      .map { line =>
        val degreeData = line.split("\\W+").map( data => Integer.parseInt(data) )
        ( degreeData(0), degreeData(1) ) }

    // make triangle number data to RDDs.
    val numTriangles = sc.textFile( args(1) )
      .map { line =>
        val triangleData = line.split("\\W+").map( data => Integer.parseInt(data) )
        ( triangleData(0), triangleData(1) ) }

    // get clustering coefficient and save as text file.
    numTriangles.join(degrees)
      .map{ data =>
        if (data._2._2 == 1) data._1 + "\t" + 0                     // if degree == 1: cc = 0
        else data._1 + "\t" + (data._2._1.toDouble / data._2._2) }  // else cc = t(u) / d(u)
      .saveAsTextFile( args(2) )

  } // end main.

} // end Task4.
