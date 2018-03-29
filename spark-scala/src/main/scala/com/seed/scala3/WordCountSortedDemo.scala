package com.seed.scala3

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object WordCountSortedDemo {
   def main(args:Array[String]):Unit={
     wordCountSortDesc
   }
   def wordCountSortDesc(){
     val sc = getSparkContext()
     val rdd = sc.textFile("sz000006_2018-03-13", 1).filter { line => line.split("\t").length == 6 }.filter { line => line.startsWith("0") || line.startsWith("1") }
     val map_rdd = rdd.map { line => (line.split("\t")(5),1) }
     val reduce_rdd = map_rdd.reduceByKey(_ + _)
     val reverse_rdd = reduce_rdd.map(tuple => (tuple._2,tuple._1))
     val sorted_rdd = reverse_rdd.sortByKey().map(tuple => (tuple._2,tuple._1))
     sorted_rdd.foreach(tuple => println(tuple._1 + " ===>>> " + tuple._2))
   }
   def getSparkContext():SparkContext={
     val conf = new SparkConf().setAppName("word count sort").setMaster("local[2]")
     val sc = new SparkContext(conf)
     sc.setLogLevel("WARN")
     sc
   }
}