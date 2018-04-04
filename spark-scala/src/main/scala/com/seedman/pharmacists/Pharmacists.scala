package com.seedman.pharmacists

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Pharmacists {
  def main(args:Array[String]):Unit={
    val conf = new SparkConf().setAppName("phar").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val phars = sc.textFile("licensed_pharmacist.txt", 1)
//    .filter { line => line.length() > 3 }
    phars.foreach { line => println(line) }
    println(phars.count())
  }
}