package com.seed.wordcount

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object WordCountDemo1 {
  def main(args:Array[String]):Unit={
      val conf = new SparkConf().setAppName("anothor helloworld ").setMaster("local[2]")
      val sc = new SparkContext(conf)
      sc.setLogLevel("WARN")
      val words =  sc.textFile("D:\\mac_idfa.txt", 1)
      println(words.first())
      println("count of words ===>>> " + words.count())
      val lines_words  = words.flatMap { line => line.split(",") }
      println(lines_words.first())
      println("count of lines words ===>>> " + lines_words.count())
      val maps = lines_words.map { word => (word,1) }
      val wordcounts = maps.reduceByKey(_ + _)
      println(wordcounts.first())
//      wordcounts.foreach(tuple => println(tuple._1 + "==>> " + tuple._2))
      
  }
  
}