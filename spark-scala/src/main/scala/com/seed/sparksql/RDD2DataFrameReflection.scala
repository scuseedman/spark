package com.seed.sparksql

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

object RDD2DataFrameReflection {
  def main(args:Array[String]):Unit={
    val conf = new SparkConf().setAppName("reflect df ").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val sqlContext = new SQLContext(sc)
    
    val stuDF = sc.textFile("students", 1).filter { line => line.split(",").length == 3 }
    
  }
  class Student(id:Int,name:String,age:Int)
}