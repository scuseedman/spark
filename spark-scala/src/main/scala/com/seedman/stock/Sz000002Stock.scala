package com.seedman.stock

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import java.util.regex.Pattern
import org.apache.hadoop.mapred.TextInputFormat
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.Text
/**
 * 股票数据输入分析，原始编码为gbk,输入时需要指定编码
 */
object Sz000002Stock {
  def main(args:Array[String]):Unit={
    val conf = new SparkConf().setMaster("local[2]").setAppName("stock")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    
    val rdd = sc.hadoopFile("sz000002_2018-02-28.xls", classOf[TextInputFormat], classOf[LongWritable], classOf[Text], 1).map(p => new String(p._2.getBytes, 0, p._2.getLength, "GBK"))
    println(rdd.count())
//    rdd.map { word => (word) }.collect.foreach(println)
    println(rdd.first())
    val fil_rdd = rdd.filter { line => line.startsWith("0") || line.startsWith("1") }
    println(fil_rdd.count)
    println(fil_rdd.first())
    
    
    
  }
}