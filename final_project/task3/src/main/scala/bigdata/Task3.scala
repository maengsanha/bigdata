package bigdata

import org.apache.spark.{SparkConf, SparkContext}

object Task3 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Task3")
    val sc = new SparkContext(conf)

    // make edge list to RDDs.
    val edges = sc.textFile( args(0) )
                  .repartition(120)
                  .map { line =>
                    val edge = line.split(" ").map(x => Integer.parseInt(x) )
                    ( edge(0), edge(1) ) }

    // indicate edge
    val copiedEdges = edges.flatMap(edge => List( (edge, -1), ( (edge._2, edge._1), -1 ) ) )    // (u, v) -> ( (u, v), -1), ( (v, u), -1 )

    // make degree RDDs.
    val degrees = edges.flatMap( edge => List( (edge._1, 1), (edge._2, 1) ) )
      .reduceByKey(_ + _)

    // sort edges by degree and value.
    val orderedEdges = edges.join(degrees)                                     // (u, (v, d(u) ) )
                            .map(data => (data._2._1, (data._1, data._2._2)) ) // ( v, (u, d(u) ) )
                            .join(degrees)                                     // (v, ( (u, d(u) ), d(v) ) )
                            .map { data =>                                     // order by degree and then value
                              if ( data._2._1._2 < data._2._2 || ( data._2._1._2 == data._2._2 && data._2._1._1 < data._1 ) ) (data._2._1, (data._1, data._2._2) )
                              else ((data._1, data._2._2), data._2._1) }

    // make ordered wedge RDDs.
    val orderedWedges = orderedEdges.join(orderedEdges)
                                    .filter{wedgeList =>
                                      wedgeList._2._1._2 < wedgeList._2._2._2 || (wedgeList._2._1._2 == wedgeList._2._2._2 && wedgeList._2._1._1 < wedgeList._2._2._1) }
                                    .map(wedge => ( (wedge._2._1._1, wedge._2._2._1), wedge._1._1) )

    // match triangles by joining wedges and edges and save as text file.
    orderedWedges.join(copiedEdges)                                                                             // ( (u, v), (w, -1) )
                .flatMap(triangle => List( (triangle._1._1, 1), (triangle._1._2, 1), (triangle._2._1, 1) ))    // [ (u, 1), (v, 1), (w, 1) ]
                .reduceByKey(_ + _)
                .map(data => data._1 + "\t" + data._2)
                .saveAsTextFile( args(1) )

  } // end main.

} // end Task3.
