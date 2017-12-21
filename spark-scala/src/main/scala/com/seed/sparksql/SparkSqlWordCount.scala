package com.formax.sql

import org.apache.hadoop.conf.Configuration
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.types.{StringType, StructField, StructType}
object SparkSqlWordCount {
  def main(args: Array[String]): Unit = {
		  val conf = new SparkConf()//创建spark conf 对象
      conf.setMaster("local[2]").setAppName("sparksql wordcount")//设置master与app name。如果在windows环境下，不设置master会报错！
		  val sc = new SparkContext(conf)//创建一个sparkcontext上下文对象
      sc.setLogLevel("WARN")
		  val sqlContext = new SQLContext(sc)
		  
		  val rddPeople = sc.textFile(args(0))
//		  val rddPeople = sc.textFile("hdfs://hadoop02:8020/data/output/stu/people.txt")
		  
		  val schemaString = "name age"
		  val schema = StructType(schemaString.split(" ").map(fieldName => StructField(fieldName, StringType, true)))
		  val rowRDD = rddPeople.map(_.split(" ")).map(p => Row(p(0), p(1)))
      
		  val people = sqlContext.createDataFrame(rowRDD, schema)
		  
		  people.registerTempTable("People")
		  
		  val peopleNames = sqlContext.sql("SELECT name FROM People")
		  peopleNames.map(t => "Name: " + t(0)).collect().foreach(println)
//r1.toDebugString
		  
		  val peoplemsg = sqlContext.sql("SELECT name,age FROM People ORDER BY age")
		  
		  peoplemsg.map(t => t(0) + "," + t(1)).collect().foreach(println)
		  
		  sc.stop()
  }
}