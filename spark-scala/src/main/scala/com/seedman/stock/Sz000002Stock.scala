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
    val conf = new SparkConf().setAppName("sto")
      val sc = new SparkContext(conf)
      sc.setLogLevel("WARN")
      
//      val rdd = sc.hadoopFile("sz000002_2018-02-28.xls", classOf[TextInputFormat], classOf[LongWritable], classOf[Text], 1).map(p => new String(p._2.getBytes, 0, p._2.getLength, "GBK"))
      val rdd = sc.textFile("file:///data/formax_data/data_test/sz000002_2018-03-05", 1)  
      val rdd1 = sc.textFile("file:///data/formax_data/data_test/sz000002_2018-03-06", 1)
      val fil_rdd = rdd.filter { line => line.startsWith("0") || line.startsWith("1") }
      val fil_rdd1 = rdd1.filter { line => line.startsWith("0") || line.startsWith("1") }
      println(fil_rdd1.count())//job1 stage1(count)
      println(fil_rdd1.sample(true, 0.1, 0).count()) //job2 stage2(count)
      val split_rdd = fil_rdd.filter { line => line.split("\t").length == 6 }.cache()
      val trades = split_rdd.map { line => (line.split("\t")(5),line.split("\t")(3).toFloat) }.reduceByKey(_ + _) //stage3(reduceByKey)
      val trades1 = fil_rdd1.map { line => (line.split("\t")(5),line.split("\t")(3).toFloat) }.reduceByKey(_ + _)//stage5(reduceByKey)
      trades.foreach(println)//job3 stage4(foreach)
      trades1.foreach(println)//job4  stage6(foreach)
      val final_trades = trades.union(trades1)  //union算子操作
      final_trades.foreach(println)//job5  stage7(foreach)
      // job6 stage8(reduceByKey) stage9(foreach) 
      final_trades.map(tuple => if(tuple._1.equals("买盘")) (1,tuple._2) else if (tuple._1.equals("卖盘")) (2,tuple._2) else (3,tuple._2)).reduceByKey(_ + _).foreach(println)
      
    
    
    
  }
}