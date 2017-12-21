package com.formax.spark_hello

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD  

object WordCount {
  def main(args: Array[String]): Unit = {

    // 设置Spark的序列化方式
    System.setProperty("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

    // 初始化Spark
    /** 
     * 第一步:创建Spark的配置对象SparkConf，设置Spark程序的运行时的配置信息 
     * 例如说通过setMaster来设置程序要连接的Spark集群的Master的URL 
     * 如果设置为local，则代表Spark程序在本地运行，特别适合于配置条件的较差的人 
     */  
    val sparkConf = new SparkConf().setAppName("wordcount")//设置master,windows环境调试时可以设置为local，设置应用名称
    /** 
     * 第二步:创建SparkContext对象 
     * SparkContext是Spark程序所有功能的唯一入口，无论是采用Scala，Java，Python等都必须有一个SparkContext 
     * SparkContext核心作用：初始化Spark应用程序运行所需要的核心组件，包括DAGScheduler，TaskScheduler，Scheduler 
     * 同时还会负责Spark程序往Master注册程序等 
     * SparkContext是整个Spark应用程序中最为至关重要的一个对象。 
     */  
    val sc = new SparkContext(sparkConf) //创建SparkContext对象，通过传入SparkConf实例来定制Spark运行的具体参数和配置信息
    sc.setLogLevel("WARN")
     /** 
     * 第三步:根据具体的数据来源（HDFS，HBase，Local FS（本地文件系统） ，DB，S3（云上）等）通过SparkContext来创建RDD 
     * RDD的创建基本有三种方式，根据外部的数据来源（例如HDFS），根据Scala集合，由其他的RDD操作产生 
     * 数据会被RDD划分成为一系列的Partitions，分配到每个Partition的数据属于一个Task的处理范畴 
     */  
      
     //文件的路径，最小并行度（根据机器数量来决定）  
    //val lines:RDD[String]= sc.textFile("D:\\soft\\spark-1.6.0-bin-hadoop2.6\\README.md", 1)    //读取本地文件，并设置Partition = 1  
    // 读取文件
    val rdd = sc.textFile(args(0),1)
    println(args(0) + " 的行数为：" + rdd.count())
    println("the first is : " + rdd.first())
    /** 
     * 第四步:对初始的RDD进行Transformation级别的处理，例如map，filter等高阶函数等的编程，来进行具体的数据计算 
     *    4.1:将每一行的字符串拆分成单个的单词 
     *    4.2:在单词拆分的基础上对每个单词的实例计数为1，也就是word =>(word,1) 
     *    4.3:在每个单词实例计数为1基础之上统计每个单词在文件出现的总次数 
     */  
      
    //对每一行的字符串进行单词的拆分并把所有行的拆分结果通过flat合并成为一个大的单词集合 
    val words = rdd.flatMap { x => x.split(" ") }
    val pairs = words.map{word => (word,1)}
    val res = pairs.reduceByKey(_+_)       //对相同的key，进行value的累加（包括Local和Reducer级别同时Reduce）
    res.foreach(wordNumberPair => println(wordNumberPair._1 + " : " + wordNumberPair._2)) 
    println("============== 萌萌的不是分割线 ====")
    val list = res.collect();
    for(elem <- list){
      println(elem._1 + " : " + elem._2)
    }
    sc.stop()  //注意一定要将SparkContext的对象停止，因为SparkContext运行时会创建很多的对象
  
  }
}