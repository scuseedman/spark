package com.seedman.stock

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object CountTotalTrades {
  def main(args:Array[String]):Unit={
    val sc = getSC()
    val lines = sc.textFile("sz000002_2018-03-21", 1).
      filter { line => line.split("\t").length == 6 && (line.startsWith("0") || line.startsWith("1")) }
//    .filter { line => line.split("\t")(5).indexOf("买盘") != -1 }
    val map_trades = lines.map { line => (line.split("\t")(5),line.split("\t")(4).toDouble/10000) }
    val reduce_trades = map_trades.reduceByKey(_ + _)
    reduce_trades.foreach(println)
    val total = reduce_trades.map(tuple => (1,tuple._2))
    val re_total = total.reduceByKey(_ + _)
    println("=======")
    re_total.foreach(println)
    println("----------------------------------------------------")
  }
  def getSC():SparkContext={
    val conf = new SparkConf().setAppName("count trades").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    sc
    
  }
}