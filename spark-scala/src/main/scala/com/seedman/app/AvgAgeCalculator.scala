package com.seedman.app

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object AvgAgeCalculator {
  def main(args:Array[String]){
    if(args.length < 1){
      println("Usage:AvgAgeCalculator datafile")
      System.exit(1)
    }
    val conf = new SparkConf().setAppName("avg age caculator").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val dataFile = sc.textFile(args(0), 1)
    val ageData = dataFile.map { line => line.split(" ")(1) }
    println(ageData.first())
    val count = dataFile.count()
    val totalAges = ageData.map ( age => Integer.parseInt(String.valueOf(age))).collect().reduce((a,b) => a + b)
    println("totalAges ===>>> " + totalAges + " ; count of peoples ===>>> " + count)
    val avgAge = totalAges.toDouble / count.toDouble
    println("aveAge ===>>> " + avgAge)
     
  }
}