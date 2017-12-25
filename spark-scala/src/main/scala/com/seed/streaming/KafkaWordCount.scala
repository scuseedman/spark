package com.seed.streaming

import org.apache.spark.streaming.kafka._
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.SparkContext._



object KafkaWordCount {
  def main(args: Array[String]): Unit = {
//    if (args.length < 4) {
//      System.err.println("Usage: KafkaWordCount <zkQuorum> <group> <topics> <numThreads>")
//      System.exit(1)
//    }
 
    val zkQuorum = args(0);
    val group = "47";
    val topicss = "cs_finance";
    val numThread = "2";
//    StreamingExamples.setStreamingLogLevels()
//    val Array(zkQuorum, group, topics, numThreads) = args
    val sparkConf = new SparkConf().setAppName("KafkaWordCount").setMaster("local[2]")
    val ssc =  new StreamingContext(sparkConf, Seconds(2))
    
    ssc.sparkContext.setLogLevel("WARN")
    ssc.checkpoint("checkpoint")
    val topicMap = topicss.split(",").map((_,numThread.toInt)).toMap
    val lines = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap).map(_._2)
    val words = lines.flatMap(_.split(" "))
    val wordCounts = words.map(x => (x, 1L))
      .reduceByKeyAndWindow(_ + _, _ - _, Minutes(10), Seconds(2), 2)
    wordCounts.print()
 
    ssc.start()
    ssc.awaitTermination()
  }
}