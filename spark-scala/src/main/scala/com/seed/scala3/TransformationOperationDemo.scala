package com.seed.scala3

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object TransformationsOperationDemo {
  def main(args:Array[String]):Unit={
//    map
//    filter
//    flatMap
//    groupByKey
//    reduceByKey
//    sortByKeyDemo
    joinedDemo
  }
  /**
   * join cogroup 
   */
  def joinedDemo(){
    val sc = getSC()
    val names = Array(("zhangfei",23),("guanyu",26),("zhaoyu",21),("huangai",33),("liubei",42))
    val ages = Array(("zhangfei",53),("guanyu",66),("zhaoyu",71),("huangai",79))
    val names_rdd = sc.parallelize(names, 1)
    val ages_rdd = sc.parallelize(ages, 1)
    //cogroup 与join不同，相当于是一个key join上的所有value全部放入一个iterable里面了。
    val joined_rdd = names_rdd.cogroup(ages_rdd)
    joined_rdd.foreach(people => println("name ==>> " + people._1 + " and score  ==>> " + people._2._1  + " and age ===>>> " + people._2._2))
    
  }
  def sortByKeyDemo(){
    val sc = getSC()
    val students = Array(("zhengfei",82),("guanyu",90),("zhaoyu",97),("liubei",65))
    val rdd = sc.parallelize(students, 1)
    rdd.foreach(student => println(student))
    println(" ========================")
    val reverse_rdd = rdd.map(tuple => (tuple._2,tuple._1))
    val sorted_rdd = reverse_rdd.sortByKey()
    val res_rdd = sorted_rdd.map(student => (student._2,student._1))
    res_rdd.foreach(student => println(student._1 + " ===>>> " + student._2))
  }
  def getSC():SparkContext={
    val conf = new SparkConf().setAppName("transformation demo").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    sc
  }
  def reduceByKey(){
    val sc = getSC()
    val rdd = sc.textFile("sz000002_2018-03-06", 1).filter { line => line.startsWith("0") || line.startsWith("1") }.filter { line => line.split("\t").length == 6 }
    
    println(rdd.first())
    val map_rdd = rdd.map { line => (line.split("\t")(5),line.split("\t")(3).toDouble) }
    val reduce_rdd  = map_rdd.reduceByKey(_ + _)
    reduce_rdd.foreach(line => println(line._1 + " ===>>> " + line._2))
    
  }
  def groupByKey(){
    val conf = new SparkConf().setAppName("group by ").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
//    val rdd = sc.textFile("sz000002_2018-03-06", 1)
    val r1 = Array(("c1",28),("c2",33),("c1",24),("c2",82))
    val rdd = sc.parallelize(r1, 1)
    val g_rdd = rdd.groupByKey()
    g_rdd.foreach(line => {println(line._1);line._2.foreach { num => println(num) }; println(" ===============>>>>>>>>>>>>>>>>>>>>>")})
    
    
  }
  def map():Unit={
    val conf = new SparkConf().setAppName("map demo").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val nums = Array(1,2,3,4,5,6)
    val rdd = sc.parallelize(nums, 1)
    val res = rdd.map { num => math.pow(num, 2) }
    res.foreach { num => println(num) }
  }
  def filter(){
    val conf = new SparkConf().setAppName("filter demo").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val nums = Array(1,3,4,6,9,11,23,24)
    val rdd = sc.parallelize(nums, 1)
    val f_rdd = rdd.filter { num => num%3 == 0 }
    f_rdd.foreach { num => println(math.pow(num, 3)) }
  }
  def flatMap(){
    val conf = new SparkConf().setAppName("flat map demo").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val rdd = sc.textFile("input.txt", 1)
    val flat_rdd = rdd.flatMap { line => line.split(" ") }
    flat_rdd.foreach { word => println(word) }
  }
}
