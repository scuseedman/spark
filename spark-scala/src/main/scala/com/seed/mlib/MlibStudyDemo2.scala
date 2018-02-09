package com.seed.mlib

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.mllib.util.KMeansDataGenerator

object MlibStudyDemo2 {
  def main(args:Array[String]):Unit={
		  val sc = getSparkContext("local[2]")
//			columnCount(sc)
//      chiSquartPersons(sc)
      kmeansRdd(sc)
  }
  def kmeansRdd(sc:SparkContext){
    val kmeansRdd = KMeansDataGenerator.generateKMeansRDD(sc, 40, 5, 3, 1.0, 2)
    println(kmeansRdd.count())
    println(kmeansRdd.first())
    val arr = kmeansRdd.take(5)
    for (x <- 0 to arr.length - 1 ){
      println(arr(x))
    }
  }
  /**
   * 结果返回:统计量为person 自由度为1 值为5.48 概率为0.019
   */
  def chiSquartPersons(sc:SparkContext){
    val v1 = Vectors.dense(43.0,9.0)
    val v2 = Vectors.dense(44.0 , 4.0)
    val c1 = Statistics.chiSqTest(v1,v2)
    println(c1)
  }
  /**
   * 计算每列的最大值，最小值，平均值，方差值，L1范数，L2范数
   */
  def columnCount(sc:SparkContext){
    val rdd = sc.textFile("mlib.txt").map { _.split("\t") }.map { f  => f.map { f  => f.toDouble } }
    val data1 = rdd.map { f  => Vectors.dense(f) }
    val stat1 = Statistics.colStats(data1)
    println(stat1.max)//每列最大值
    println(stat1.min)//每列最小值
    println(stat1.mean)//每列平均值
    println(stat1.variance)//方差值
    println(stat1.normL1)//L1范数
    println(stat1.normL2)//L2范数
    println(rdd.partitions.size)
  }
  def getSparkContext(master:String):SparkContext={
    val conf = new SparkConf().setAppName("column count").setMaster(master)
    val sct = new SparkContext(conf)
    sct.setLogLevel("WARN")
    sct
  }
}