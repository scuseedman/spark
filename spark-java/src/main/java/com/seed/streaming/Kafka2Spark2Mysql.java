/** 
 * Project Name:spark-java 
 * File Name:Kafka2Spark2Mysql.java 
 * Package Name:com.seed.streaming 
 * Date:2017年12月25日下午3:24:49 
 * Copyright (c) 2017,  All Rights Reserved. 
 * 
* 　　　　　　　　┏┓　　　┏┓
 * 　　　　　　　┏┛┻━━━┛┻┓
 * 　　　　　　　┃　　　　　　　┃ 　
 * 　　　　　　　┃　　　━　　　┃
 * 　　　　　　　┃　＞　　　＜　┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃...　⌒　... ┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃　　　　　　　　　　
 * 　　　　　　　　　┃　　　┃   神兽无影，BUG无踪！
 * 　　　　　　　　　┃　　　┃　　　　　　　　　　　
 * 　　　　　　　　　┃　　　┃  　　　　　　
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　　　　　　　　　　
 * 　　　　　　　　　┃　　　┗━━━┓
 * 　　　　　　　　　┃　　　　　　　┣┓
 * 　　　　　　　　　┃　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛
*
*
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛Code is far away from bug with the animal protecting
 * 　　　　┃　　　┃    神兽无影，BUG无踪！
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 *
 * ━━━━━━感觉萌萌哒━━━━━━
*/ 
package com.seed.streaming;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import scala.Tuple2;

/** 
 * ClassName:Kafka2Spark2Mysql
 * Date:     2017年12月25日 下午3:24:49 
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 * offset程序并未记录到正确的，需要重构此代码 _seed
 */
public class Kafka2Spark2Mysql {
    public static String group = "47";
    public static String topics = "cs_finance";
    public static Integer numThread = 2;
	public static void main(String[] args) {
		String zkQuorum = args[0];
		SparkConf sparkConf = new SparkConf().setAppName("stea java").setMaster(args[1]);
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);
		ctx.setLogLevel("WARN");
		JavaStreamingContext jsc = new JavaStreamingContext(ctx, new Duration(2000));
		Map<String,Integer> topicMap = new HashMap<String,Integer>();
		for(String topic :topics.split(",")){
			topicMap.put(topic, numThread);
		}
		/**
		 * 从kafka中获取数据，消费kafka的数据，这种模式消费有问题啊，每次都把历史数据消费出来 了，而不是最新的，也就是说
		 * 没有记录到最新的offset。该 offset应该在zk中有记录才对啊，应该使用kafka 的high api
		 */
		JavaPairReceiverInputDStream<String, String> messages = KafkaUtils.createStream(jsc, zkQuorum, group, topicMap);//context,zk,group,topics
		JavaDStream<String> lines = messages.map(new Function<Tuple2<String,String>, String>() {
			private static final long serialVersionUID = 1L;
			public String call(Tuple2<String, String> tuple) throws Exception {
				System.out.println("after kafka ===> " + tuple._2);
				return tuple._2;
			}
		});
		JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
			private static final long serialVersionUID = 1L;
			public Iterable<String> call(String line) throws Exception {
				return Arrays.asList(line.split(","));
			}
		});
		JavaPairDStream<String, Integer> lastCounts = words.mapToPair(new PairFunction<String, String, Integer>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, Integer> call(String word) throws Exception {
				return new Tuple2<String,Integer>(word,1);
			}
		});
		JavaPairDStream<String, Integer> nowCounts = lastCounts.reduceByKey(new Function2<Integer, Integer, Integer>() {
			private static final long serialVersionUID = 1L;
			public Integer call(Integer count1, Integer count2) throws Exception {
				return count1 + count2;
			}
		});
		nowCounts.print();
		jsc.start();
		jsc.awaitTermination();
		ctx.close();
		
	}
}
