package com.seedman.pharmacists

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object CountPharmacists {
  def main(args:Array[String]):Unit={
    val conf = new SparkConf().setAppName("phars").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val phars = sc.textFile("E:\\chrome_download\\stu5.gz", 1)
    println(phars.count())
    println(phars.first())
    val percent = sc.textFile("E:\\formax_消费金融_大数据\\uids_percent.txt", 1).map { line => (line,1) }
    println(percent.first())
    val map_phars = phars.map { line => (line.split("\001")(0),line.split("\001")(1)) }
    val joined = percent.join(map_phars)
    println(joined.first()._1)
    println(joined.count())
  }
}