package com.seed.sparksql_scala

import org.apache.spark.SparkConf
import org.apache.spark.sql.SQLContext
import org.apache.spark.SparkContext

object DataFrameOperation {
  def main(args:Array[String]):Unit={
    val conf = new SparkConf().setAppName("df demo").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val sqlContext = new SQLContext(sc);
    val df  = sqlContext.read.json("student.json")
    println(df.first())
    println("----------------------")
    df.show()
    df.printSchema()
    df.select("name").show()
    println("=================================萌萌的分割线 ??")
    df.select(df("name"),df("age") + 1).show()
    println("=================================萌萌的分割线 ??")
    df.filter(df("age") > 50).select("id", "name","age").show()
    println("=================================萌萌的分割线 ??")
    df.groupBy("age").count().show()
  }
}