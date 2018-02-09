package com.seed.core

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import java.util.regex.Pattern
/**
 * 处理直辖市的市级位置
 */
object MunicipalityDeal {
  val fourCities = "北京 上海 天津 重庆"
  def main(args:Array[String]):Unit={
    val conf = new SparkConf().setAppName("Municipality").setMaster("local[2]")
    val sc =  new SparkContext(conf)
    sc.setLogLevel("WARN")
    val mobiles = sc.textFile("another_crawler_success", 1).filter { line => line.split(",").length == 9 }//filter 长度限制
    println("mobiles ===>>> " + mobiles.count())
    println(mobiles.first())
    val m_mobiles = mobiles.map { line => dealMunicipality(line) }
    println("m_mobiles ===>>> " + m_mobiles.first())
    m_mobiles.saveAsTextFile("municipalitiedMobiles")
    
  }
  def dealMunicipality(line:String):String={
    println("fourCities ===>>> " + fourCities)
    println("line(2) ===>>> " + line.split(",")(2))
    if(fourCities.contains(line.split(",")(2))){
      println("===================>>>>>>>>")
      println(" line ===> " + line)
      var arr = line.split(",")
      arr(3) = arr(2)
      println(arr.mkString(","))
      arr.mkString(",")
    }else{
      line
    }
    
  }
}