package com.seed.scala3

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object PartitionByDemo {
  def main(args:Array[String]):Unit={
    val sc = getSparkContext()
    var rdd1 = sc.makeRDD(Array((1,"A"),(2,"B"),(3,"C"),(4,"D")),2)
    println(rdd1.partitions.size)
    val c2 = rdd1.mapPartitionsWithIndex{
              (partIdx,iter) => {
                var part_map = scala.collection.mutable.Map[String,List[(Int,String)]]()
                  while(iter.hasNext){
                    var part_name = "part_" + partIdx;
                    var elem = iter.next()
                    if(part_map.contains(part_name)) {
                      var elems = part_map(part_name)
                      elems ::= elem
                      part_map(part_name) = elems
                    } else {
                      part_map(part_name) = List[(Int,String)]{elem}
                    }
                  }
                  part_map.iterator
                 
              }
            }.collect
    for ( c <- c2){
      println(c)
    }
    println("---------------------------------------------- 萌萌的分割线 --------------------------------")
    var rdd2 = rdd1.partitionBy(new org.apache.spark.HashPartitioner(2))
    println(rdd2.partitions.size)
    val c1 = rdd2.mapPartitionsWithIndex{
              (partIdx,iter) => {
                var part_map = scala.collection.mutable.Map[String,List[(Int,String)]]()
                  while(iter.hasNext){
                    var part_name = "part_" + partIdx;
                    var elem = iter.next()
                    if(part_map.contains(part_name)) {
                      var elems = part_map(part_name)
                      elems ::= elem
                      part_map(part_name) = elems
                    } else {
                      part_map(part_name) = List[(Int,String)]{elem}
                    }
                  }
                  part_map.iterator
              }
            }.collect
    for(c <- c1){
      println(c)
    }
  }
  def getSparkContext():SparkContext={
    val conf = new SparkConf().setAppName("partitionby").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    sc
  }
}