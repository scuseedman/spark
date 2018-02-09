package com.seed.core

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object MobileBelongDeal {
  def main(args:Array[String]):Unit={
    val conf = new SparkConf().setAppName("mobile deal").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val mobiles = sc.textFile("another_crawler_success", 1).filter { line => line.split(",").length == 9 }
    println("count mobiles ===>>> " + mobiles.count())
    println("length of mobiles ===>>> " + mobiles.first().split(",").length)
    val area_codes = sc.textFile("national_codes", 1)
    println("count of area_codes ===>>> " + area_codes.count())
    println(mobiles.first())
    println(area_codes.first())
    val codes = area_codes.map( line => (line.split("\t")(1),line.split("\t")(0)))
    println(codes.first())
    val city_mbs = mobiles.map { line => (line.split(",")(3),line) }
    println(city_mbs.first())
    val mobiles_res = city_mbs.join(codes)
    println(mobiles_res.first())
    val res = mobiles_res.map(line => (line._2._1 + "," + line._2._2))    
    println(res.first())
    val final_res = res.map { line => (line.split(",")(0).substring(0,3) + "," + line.split(",")(1) + "," + line.split(",")(2) + "," + line.split(",")(3) + "," + line.split(",")(4) + "," + line.split(",")(5) + "," + line.split(",")(6) + "," + line.split(",")(9) + "," + line.split(",")(8)) }
    println(final_res.first())
    final_res.saveAsTextFile("all_dealed_mobiles_belong")
  }
}