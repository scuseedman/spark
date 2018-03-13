package com.seedman.stock

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapred.TextInputFormat

object SingleStockAnaly {
  def main(args:Array[String]):Unit={
    val conf = new SparkConf().setAppName("single").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val stocks = sc.textFile("sz001979_2018-03-12.xls", 1).filter { line => line.startsWith("0") || line.startsWith("1") }.cache()
//    val stocks1 = sc.textFile("sz000002_2018-03-07", 1).filter { line => line.startsWith("0") || line.startsWith("1") }.cache()
//    val stocks2 = sc.textFile("sz000002_2018-03-06", 1).filter { line => line.startsWith("0") || line.startsWith("1") }.cache()
//    val stocks3 = sc.textFile("sz000002_2018-03-05", 1).filter { line => line.startsWith("0") || line.startsWith("1") }.cache()
//    val stocks4 = sc.textFile("sz000002_2018-03-02", 1).filter { line => line.startsWith("0") || line.startsWith("1") }.cache()
//    val stocks = stocks5.union(stocks1).union(stocks2).union(stocks3).union(stocks4)
    val map_stocks = stocks.map { line => (line.split("\t")(5),(line.split("\t")(3).toFloat),line.split("\t")(4).toDouble/10000) }//(买盘，(成交量，总额))
    val hands_stocks = stocks.map { line => (line.split("\t")(5),line.split("\t")(3).toFloat) }.reduceByKey(_ + _)//手数
    val trades_stocks = stocks.map { line => (line.split("\t")(5),line.split("\t")(4).toDouble/10000) }.reduceByKey(_ + _)//交易额
    val joined = hands_stocks.join(trades_stocks)
    println("--------------------------")
    joined.foreach(println)
    val avg_joined = joined.map(line => (line._1,line._2._2*100/line._2._1))
    println("--------------------------")
    avg_joined.foreach(println)
    println("===================================================")
    
  }
}