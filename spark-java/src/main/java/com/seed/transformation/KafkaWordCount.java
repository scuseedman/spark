package com.seed.transformation;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

public class KafkaWordCount {
	public static void main(String[] args) {
		
	}
	public static void kafkaConsumerWordCount(){
		SparkConf sparkConf = new SparkConf().setAppName("kafka source").setMaster("local[2]");
		JavaStreamingContext jsc = new JavaStreamingContext(sparkConf,new Duration(5000));//5秒扫一次KAFKA数据
		//使用kafkautils.createStream()创建输入流
		
		jsc.start();
		jsc.awaitTermination();
		jsc.close();
		
	}
}
