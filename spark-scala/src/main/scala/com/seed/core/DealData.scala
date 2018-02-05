package com.seed.core

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object DealData {
  def main(args:Array[String]):Unit={
    val conf = new SparkConf().setAppName("DataDemo1").setMaster("local[2]")
    var sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    println("length of args ===>>> " + args.length)
    val input_p = args(0)//p
//    val input = readLine("输入文件地址===>>> \n")
    println("input path ===>>>  " + input_p)
//    val input_a = args(1)//a
    val input_m = args(2)//m
    val p_totals = sc.textFile(input_p, 1)//输入数据 _p
//    val a_totals = sc.textFile(input_a, 1)
    val m_totals = sc.textFile(input_m, 1)
//    println("p_totals.count ===>>> " + p_totals.count())
//    println(p_totals.first())
//    println(a_totals.first())
//    println(m_totals.first())
    val filter_p = p_totals.filter { line => line.split("\\|").length == 15 }
//    val filter_a = a_totals.filter { line => line.split(",").length == 3 }
    val filter_m = m_totals.filter { line => line.split("\t").length == 4 }
    println("===>>> " + filter_p.count())
    val maps_p = filter_p.map { line => (line.split("\\|")(0).toLowerCase(),line.split("\\|")(3)) }// p_no ,city_code || 0755ba000017,4403
//    val maps_a = filter_a.map { line => (line.split(",")(0),line.split(",")(1)) }
    val maps_m = filter_m.map { line => (line.split("\t")(0),line.split("\t")(3)) }
    println(maps_p.first())
//    println(maps_a.first())
    println(maps_m.first())
    val joined_m_p = maps_m.join(maps_p)
    
    joined_m_p.foreach(joined_m_p => {
      println(joined_m_p._2._1 + " ===>>> " + joined_m_p._2._2)
    })
    
    println(joined_m_p.first())
    val mos = joined_m_p.map(line => (line._2._1,line._2._2))
    println(mos.first)
    
    
    
    
    
  }
  
}