package com.seedman.stock

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
/**
 * 测试partitions by 作用
 */
object PartitionsBy {
  def main(args:Array[String]):Unit={
    val sc = getSparkContext()
    val lines = sc.textFile("sz000002_2018-03-20", 2).filter { line => line.split("\t").length == 6 && (line.startsWith("0") || line.startsWith("1")) }
    val map_trades = lines.map { line => (line.split("\t")(5),line) }
  }
  def getSparkContext():SparkContext={
    val conf = new SparkConf().setAppName("partitions by ").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    sc
  }
}