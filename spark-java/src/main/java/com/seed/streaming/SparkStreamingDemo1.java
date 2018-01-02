/** 
 * Project Name:spark-java 
 * File Name:Kafka2SparkStreaming2Redis.java 
 * Package Name:com.seed.streaming 
 * Date:2017年12月28日上午11:18:03 
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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kafka.serializer.StringDecoder;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.api.java.function.VoidFunction2;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Time;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import com.seed.service.impl.RedisServiceImpl;

import scala.Tuple2;

/** 
 * ClassName:Kafka2SparkStreaming2Redis
 * Date:     2017年12月28日 上午11:18:03 
 * DESC:	数据流向：kafka => spark streaming => redis
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class SparkStreamingDemo1 {
	private static final SimpleDateFormat sdf =  new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
	public static void main(String[] args){
		System.out.println("should input 3 args ; 1、broker list ; 2、master mode ; 3、redis host");
		if(args.length != 3)
			System.exit(1);
		kafka2Streaming2Redis(args[0],args[1],args[2]);//brokerLists, masterName
		
	}
	/**
	 * 数据流向 kafka => streaming => redis
	 * @param brokerLists	kafka brokerlists
	 * @param masterName	master on local or yarn ?
	 */
	@SuppressWarnings("deprecation")
	public static void kafka2Streaming2Redis(String brokerLists,String masterName,final String redisHost){
		System.out.println(" ============>>> start ");
		SparkConf sparkConf = new SparkConf().setAppName("kafka2streaming").setMaster(masterName);
		sparkConf.set("spark.streaming.stopGracefullyOnShutdown","true");//设置钩子，使之可以优雅的关停掉这个sparkstreaming程序
		JavaStreamingContext jsc = new JavaStreamingContext(sparkConf,new Duration(10000));//10s
		
		JavaSparkContext ctx = jsc.sparkContext();
		ctx.setLogLevel("WARN");
		Map<String,String> kafkaParams = new HashMap<String,String>();//kafka 需要的参数
		kafkaParams.put("metadata.broker.list", brokerLists);
		kafkaParams.put("group.id", "47");
		Set<String> topics = new HashSet<String>();//topic 集合
		topics.add("cs_finance");
		JavaPairInputDStream<String, String> inputStream = KafkaUtils.createDirectStream(//从kafka中读取数据出来
				jsc, 
				String.class, 
				String.class, 
				StringDecoder.class,
				StringDecoder.class,
				kafkaParams,
				topics
				);
		JavaDStream<String> words = inputStream.flatMap(new FlatMapFunction<Tuple2<String,String>, String>() {//输入Tuple2<String,String>  ；输出：String
			private static final long serialVersionUID = 1L;
			public Iterable<String> call(Tuple2<String, String> tuple)
					throws Exception {
				System.out.println(sdf.format(new Date()) + " ===>>> " + tuple._2  );
				return Arrays.asList(tuple._2.toLowerCase().split(" "));//转换为小写
			}
		});
		JavaPairDStream< String, Integer> pairWord = words.mapToPair(new PairFunction<String, String, Integer>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, Integer> call(String word) throws Exception {
				return new Tuple2<String,Integer>(word,1);
			}
		});
		JavaPairDStream< String, Integer> wordcounts = pairWord.reduceByKey(new Function2<Integer, Integer, Integer>() {
			private static final long serialVersionUID = 1L;
			public Integer call(Integer count1, Integer count2) throws Exception {
				return count1 + count2;
			}
		});
		wordcounts.print();
		System.out.println(" <<<<<<==================");
		wordcounts.foreachRDD(new Function<JavaPairRDD<String,Integer>, Void>() {
			private static final long serialVersionUID = 1L;
			public Void call(JavaPairRDD<String, Integer> rdd) throws Exception {
				rdd.foreach(new VoidFunction<Tuple2<String,Integer>>() {
					private static final long serialVersionUID = 1L;
					public void call(Tuple2<String, Integer> wordcount) throws Exception {
						System.out.println(wordcount._1 + " ===>>> " + wordcount._2);
						new RedisServiceImpl(redisHost).putRes2Redis(wordcount);
					}
				});
				return null;
			}
		});
		jsc.start();
		jsc.awaitTermination();
		jsc.stop(true, true);
		jsc.close();
	}
}
