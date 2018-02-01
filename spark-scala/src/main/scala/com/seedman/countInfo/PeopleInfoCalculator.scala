package com.seedman.countInfo

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object PeopleInfoCalculator {
  def main(args:Array[String]):Unit={
    if(args.length < 1)System.exit(1)
    val conf = new SparkConf().setAppName("people info caculator").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val peoples = sc.textFile(args(0), 1)
    val m_peoples = peoples.filter { people => people.split(" ")(1) == "M" }.map { line => line.split(" ")(1) + " " + line.split(" ")(2) }
    val f_peoples = peoples.filter { people => people.split(" ")(1) == "F" }.map { line => line.split(" ")(1) + " " + line.split(" ")(2) }
    println(m_peoples.first())
    println(f_peoples.first())
    val m_height_data = m_peoples.map { people => people.split(" ")(1).toInt }
    val f_height_data = m_peoples.map { people => people.split(" ")(1).toInt }
    
    val lowesMale = m_height_data.sortBy(x => x,true).first()
    val lowesFeMale = f_height_data.sortBy(x => x,true).first()
    
    val highMale = m_height_data.sortBy(x => x,false).first()
    val highFeMale = f_height_data.sortBy(x => x,false).first()
    
    println("total num of male ===>>> " + m_peoples.count())
    println("total num of female ===>>> " + f_peoples.count())
    
    println("lowerMale ===>>> " + lowesMale)
    println("lowerFeMale ===>>> " + lowesFeMale)
    
    println("highest male ===>>> " + highMale)
    println("highest female ===>>> " + highFeMale)
    
    
    
    
    
  }
}