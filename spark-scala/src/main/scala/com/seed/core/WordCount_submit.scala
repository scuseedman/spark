package com.seed.core

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object WordCount_submit {
  def main(args:Array[String]):Unit={
    println(" args(0) ===>>> input file ; args(1) ===>>> master name ; split ===>>> ,")
    if(args.length != 1)System.exit(-1)
    val conf = new SparkConf().setAppName("hello world !")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val words = sc.textFile(args(0),1).flatMap { line => line.split(",") }.map { word => (word,1) }.reduceByKey(_ + _)
    words.foreach(x => println(x))
    
  }
}