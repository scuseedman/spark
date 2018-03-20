package com.seed.scala3

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import com.seed.entity.SecondarySortKey
/**
 * 基于spark的二次排序
 */
object SecondSortDemo {
  def main(args:Array[String]):Unit={
    val conf = new SparkConf().setAppName("scala secondary sorted").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val rdds = sc.textFile("sz000002_2018-03-08", 1).filter { line => (line.startsWith("0") || line.startsWith("1") ) && line.split("\t").length == 6 }
    val lines = rdds.map { line => (new SecondarySortKey(line.split("\t")(4).toInt,line.split("\t")(3).toInt),line) }
    val sorted_lines = lines.sortByKey(false)
    val res_lines = sorted_lines.map(tuple => tuple._2)
    val topn = res_lines.take(30)
    topn.foreach { line => println(line) }
  }
}