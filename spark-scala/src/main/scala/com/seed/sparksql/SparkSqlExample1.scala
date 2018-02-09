package com.seed.sparksql

import org.apache.spark.sql.SQLContext
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.hive.HiveContext

object SparkSqlExample1 {
  def main(args:Array[String]):Unit={
    val conf = new SparkConf().setMaster("local[2]").setAppName("sparksqldemo1")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
//    val sqlContext = new SQLContext(sc)
    val hiveContext = new HiveContext(sc)
    val prop = new java.util.Properties()
    prop.setProperty("user", "hive")
    prop.setProperty("password", "")
    val driverName = "org.apache.hadoop.hive.jdbc.HiveDriver";
    Class.forName(driverName);
    val url = "jdbc:hive2://hadoop03:10000/default"
    val table = "stu"
    hiveContext.sql("FROM test.stu SELECT id, name").collect().foreach(println)
    val r_table = hiveContext.read.jdbc(url, table, prop)
    var d = r_table.select("id", "name")
    println(d.first())
  }
}