package com.seedman.stock

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object SingleStock {
  def main(args:Array[String]):Unit={
    val conf = new SparkConf().setAppName("test word").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val words = sc.textFile(args(0), 1).filter { line => line.split("\t").length == 2 }
    val nums = words.map { line => (line.split("\t")(0)+ "_" + line.split("\t")(1),1) }
    println(nums.first())
    val reduce_nums = nums.reduceByKey(_ + _).map(tuple => (tuple._1.split("_")(0),tuple._1.split("_")(1)))
    println("------------------------------")
    println(reduce_nums.first())
    println("------------------------------")
    println("count of reduce_nums ===>>> " + reduce_nums.count())
    
    
    
  }
}