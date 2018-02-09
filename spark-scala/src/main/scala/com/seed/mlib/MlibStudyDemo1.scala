package com.seed.mlib

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object MlibStudyDemo1 {
  def main(args:Array[String]):Unit={
//    sampleTest
//    unionTest
//    combinerTest
  }
  
  def sampleTest(){
    val conf = new SparkConf().setAppName("sampleTest").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val a = sc.parallelize(1 to 10000 , 3)
    println(a.sample(false , 0.09, 0).count())//数据抽样 ,返回的还是rdd，而takeSample操作返回的是集合而不是rdd
    
  }
  def unionTest(){
    val conf = new SparkConf().setAppName("union").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val a = sc.parallelize(1 to 10, 1)
    val b = sc.parallelize(8 to 18, 1)
    val c = a.union(b).distinct().collect()
    println(c(13))
    println("length of c ===>>> " + c.length)
  }
  def combinerTest(){
    val conf = new SparkConf().setAppName("union").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val data = Array((1,1.0),(1,2.0),(1,3.0),(2,4.0),(2,5.0),(2,6.0))
    val a = sc.parallelize(data, 2)
    val combiner = a.combineByKey(
        createCombiner = (v:Double) =>(v:Double,1),
        mergeValue = (c:(Double,Int),v:Double) => (c._1 + v ,c._2 + 1),
        mergeCombiners =(c1:(Double,Int),c2:(Double,Int)) => (c1._1 + c2._1 , c1._2 + c2._2),
        numPartitions = 2
    ).collect()
    println(combiner(0))
    println(combiner(1))
    
    
    
  }
  def getConf(master:String):SparkContext ={
    val conf = new SparkConf().setAppName("union").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    sc
  }
}