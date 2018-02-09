package com.seed.core

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

/**
 * 数据倾斜测试
 */
object SlantDataTest {
  def main(args:Array[String]):Unit={
		  val conf = new SparkConf().setAppName("slantData").setMaster("local[2]")
			val sc = new SparkContext(conf)
		  sc.setLogLevel("WARN")
		  val names = sc.textFile("slantData.txt", 2)
		  val m_names = names.map { name => (name,1) }
		  val r_names = m_names.reduceByKey(_ + _)
			r_names.foreach(name => println(name._1,name._2))
				  
    
  }
  
  
}