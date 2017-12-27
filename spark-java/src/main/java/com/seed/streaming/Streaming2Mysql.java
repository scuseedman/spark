package com.seed.streaming;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import kafka.serializer.StringDecoder;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import com.seed.service.impl.Streaming2MysqlServiceImpl;

import scala.Tuple2;

/**
 * 将计算结果实时更新到mysql等持久化的数据库中 代码待明天测试 2017.12.26 23:10 _seed
 * 代码已经测试，可以将结果写入到mysql表中了 _2017.12.27 14:27 _seed
 * 对结果进行判断，如果存在，则更新，不存在则新增
 * 也可尝试将计算结果持久化到redis中
 * @author lwd
 *
 */
public class Streaming2Mysql {
	private static Logger logger = Logger.getLogger(Streaming2Mysql.class);
	public static void main(String[] args) {
		logger.info("===========================> ");
		putData2Mysql(args[0],args[1]);
	}
	@SuppressWarnings("deprecation")
	public static void putData2Mysql(String brokerLists,String masterName){
		SparkConf sparkConf = new SparkConf().setAppName("2mysql").setMaster(masterName);
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.setLogLevel("WARN");
		JavaStreamingContext sc = new JavaStreamingContext(jsc,new Duration(5000));
		Map<String,String> kafkaParams = new HashMap<String,String>();//kafka连接参数
		kafkaParams.put("metadata.broker.list", brokerLists);
		kafkaParams.put("group.id", "46");
		Set<String> topics = new HashSet<String>();//topics 集合
		topics.add("cs_finance");
		JavaPairInputDStream<String, String> lines = KafkaUtils.createDirectStream(
				sc, 
				String.class,
				String.class,
				StringDecoder.class,
				StringDecoder.class,
				kafkaParams, 
				topics);
		JavaDStream<String> words = lines.flatMap(new FlatMapFunction<Tuple2<String,String>, String>() {
			private static final long serialVersionUID = 1L;
			public Iterable<String> call(Tuple2<String, String> tuple)
					throws Exception {
				return Arrays.asList(tuple._2.split(" "));//以空格分隔获取得到的数据，返回一个集合
			}
		});
		JavaPairDStream<String, Integer> wordCount = words.mapToPair(new PairFunction<String, String, Integer>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, Integer> call(String word) throws Exception {
				return new Tuple2<String,Integer>(word,1);
			}
		});
		JavaPairDStream<String, Integer> res = wordCount.reduceByKey(new Function2<Integer, Integer, Integer>() {
			private static final long serialVersionUID = 1L;
			public Integer call(Integer count1, Integer count2) throws Exception {
				return count1 + count2;
			}
		});
//		res.print();
		//得到所有单词的计数后，将会使用mysql对这些统计的数据进行持久化
		//此外可以调用查询库中存在的值进行更新操作，即可以达到流计算的目的 _seed
		//方法过时 ？需要重新尝试用个新的方法进行测试 _seed
				res.foreach(new Function<JavaPairRDD<String,Integer>, Void>() {
					private static final long serialVersionUID = 1L;
					public Void call(JavaPairRDD<String, Integer> wordcountsRdd) throws Exception {
						wordcountsRdd.foreachPartition(new VoidFunction<Iterator<Tuple2<String,Integer>>>() {
							private static final long serialVersionUID = 1L;
							public void call(Iterator<Tuple2<String, Integer>> wordcounts) throws Exception {
								new Streaming2MysqlServiceImpl().putWords2Mysql(wordcounts);//service 层处理
							}
						});
						return null;
					}
				});
		
		
		sc.start();
		sc.awaitTermination();
		sc.close();
	}
}
