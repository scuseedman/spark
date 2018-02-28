package com.seed.wordcount

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

/**
 * scala版本的wordcount demo 示例程序3 从hdfs读取文件进行统计输出
 * 程序打包方式 直接export 该类jar即可
 */
object WordCountThreeDemo {
  
  def main(args:Array[String]):Unit={
    val conf = new SparkConf().setAppName("word count")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
//    val words = sc.textFile("E:\\work\\jsoup_demo\\all_failure", 1).flatMap { line => line.split("\t") }.map { word => (word,1) }
    val words = sc.textFile("/user/formax/stu.txt", 1).flatMap { line => line.split("\t") }.map { word => (word,1) }
    val r_words = words.reduceByKey(_ + _)
    r_words.foreach(tuple => println(tuple._1 + " ===>>> " + tuple._2))
    
   
  }
}