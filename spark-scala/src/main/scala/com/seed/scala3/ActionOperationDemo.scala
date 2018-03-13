package com.seed.scala3

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
/**
 * action 操作实践
 */
object ActionOperationDemo {
  def main(args:Array[String]):Unit={
    reduceDemo
  }
  def reduceDemo(){
    val sc = getSparkContext()
    val nums = Array(1,2,3,4,5,6,7,8,9,10)
    val rdd = sc.parallelize(nums, 1)
    val r_rdd = rdd.reduce((v1,v2) => add(v1,v2))
    println(r_rdd)
  }
  
  def add(v1:Integer,v2:Integer):Integer={
    v1 + v2
  }
  def getSparkContext():SparkContext={
    val conf = new SparkConf().setMaster("local[2]").setAppName("action demo ")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    sc
  }
}