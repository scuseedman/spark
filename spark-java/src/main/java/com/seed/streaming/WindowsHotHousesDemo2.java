/** 
 * Project Name:spark-java 
 * File Name:WindowsHotHousesDemo2.java 
 * Package Name:com.seed.streaming 
 * Date:2018年2月12日上午9:59:35 
 * Copyright (c) 2018,  All Rights Reserved. 
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
 * 　　　　　　　　　┃　　　┃　欣赏一个人,始于颜值,敬于智慧,久于善良,终于人品.　　　　　　　　　　
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

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import kafka.common.TopicAndPartition;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaCluster;
import org.apache.spark.streaming.kafka.OffsetRange;

import scala.Tuple2;

import com.seed.config.Global;
import com.seed.utils.OffSetUtil;

/** 
 * ClassName:WindowsHotHousesDemo2
 * Date:     2018年2月12日 上午9:59:35 
 * DESC:	滑动对一个时间窗口输入内的数据进行批量操作
 * 				kafka offset 操作使用工具类进行
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class WindowsHotHousesDemo2 implements Serializable{
	/**  */
	private static final long serialVersionUID = 1L;
	private static final Integer INTERVAL = 5000;//数据读取频率
	private static final Integer FREQUENCY = 20;//计算间隔
	private static final Integer WINDOWS = 60;//计算的时间段
	private static final Integer TOPN = 3;//取的top N
	private static final String SPLIT = ",";
	private Map<String,String> kafkaParam = null;
	 private static KafkaCluster kafkaCluster = null;
	 private static Broadcast<Map<String, String>> kafkaParamBroadcast = null;
	 private static Logger logger = Logger.getLogger(WindowsHotHousesDemo2.class);
	public static void main(String[] args) {
		String master = System.getProperty("os.name").toLowerCase().contains("windows")?"local[2]":"yarn-cluster";
		new WindowsHotHousesDemo2().start(master);
	}

	private void start(String master) {
		SparkConf conf = new SparkConf().setAppName("windows").setMaster(master);
		conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
		conf.set("spark.streaming.stopGracefullyOnShutdown","true");
		JavaSparkContext jsc = new JavaSparkContext(conf);
		jsc.setLogLevel("WARN");
		JavaStreamingContext sc = new JavaStreamingContext(jsc,new Duration(INTERVAL));//INTERVAL 秒 来一发
		sc.checkpoint("usbCheckPoint");
		kafkaParam = OffSetUtil.initKafkaParams();//初始化kafka连接参数
		kafkaCluster = OffSetUtil.initKafkaCluster(kafkaParam);//初始化kafka集群
		//初始化zk中kafka的位置信息
		Map<TopicAndPartition, Long> consumerOffsetsLong = OffSetUtil.initConsumerOffset(Global.getConfVal("KAFKA_TOPIC"),kafkaCluster,kafkaParam);
		//共享变量进行广播出去
		kafkaParamBroadcast = sc.sparkContext().broadcast(kafkaParam);
		//获取 数据
		JavaInputDStream<String> message = OffSetUtil.createKafkaDStream(sc, consumerOffsetsLong,kafkaParamBroadcast);
		final AtomicReference<OffsetRange[]> offsetRanges = new AtomicReference<OffsetRange[]>();
		JavaDStream<String> javaDStream = OffSetUtil.getAndUpdateKafkaOffset(message, offsetRanges,kafkaCluster,kafkaParamBroadcast);
		JavaDStream<String> map_rdd = javaDStream.flatMap(new FlatMapFunction<String, String>() {
			private static final long serialVersionUID = 1L;
			public Iterable<String> call(String line) throws Exception {
				return Arrays.asList(line.split(SPLIT));
			}
		});
//		.map(new Function<String, String>() {
//			private static final long serialVersionUID = 1L;
//			public String call(String msg) throws Exception {
//				return msg;
//			}
//		});
		JavaPairDStream<String,Integer> pair_rdd =  map_rdd.mapToPair(new PairFunction<String, String, Integer>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, Integer> call(String msg) throws Exception {
				return new Tuple2<String,Integer>(msg,1);
			}
		});
		JavaPairDStream<String, Integer> rp_rdd = pair_rdd.reduceByKeyAndWindow(new Function2<Integer, Integer, Integer>() {
			private static final long serialVersionUID = 1L;
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v2 + v2;
			}
		}, Durations.seconds(WINDOWS), Durations.seconds(FREQUENCY));//每FREQUENCY个时间段执行一个,计算WINDOWS时间段内获取的数据进行统计的结果
		JavaPairDStream<String, Integer> trp_rdd = rp_rdd.transformToPair(new Function<JavaPairRDD<String,Integer>, JavaPairRDD<String,Integer>>() {
			private static final long serialVersionUID = 1L;
			public JavaPairRDD<String, Integer> call(
					JavaPairRDD<String, Integer> rp_words) throws Exception {
				JavaPairRDD< Integer,String> reverse_rdd = rp_words.mapToPair(new PairFunction<Tuple2<String,Integer>, Integer, String>() {
					private static final long serialVersionUID = 1L;
					public Tuple2<Integer, String> call(
							Tuple2<String, Integer> tuple) throws Exception {
						// 对这个元组数据进行反转,以便进行排序
						return new Tuple2<Integer,String>(tuple._2,tuple._1);
					}
				});
				JavaPairRDD<Integer, String>  sorted_words =  reverse_rdd.sortByKey(false);
				JavaPairRDD<String, Integer> normal_rdd = sorted_words.mapToPair(new PairFunction<Tuple2<Integer,String>, String, Integer>() {
					private static final long serialVersionUID = 1L;
					public Tuple2<String, Integer> call(
							Tuple2<Integer, String> tuple) throws Exception {
						return new Tuple2<String,Integer>(tuple._2,tuple._1);
					}
					
				});
				List<Tuple2<String,Integer>> res = normal_rdd.take(TOPN);
				for(Tuple2<String,Integer> tuple:res){
					logger.warn(" key --->>> " + tuple._1 + " ; value --->>> " + tuple._2);
				}
				return normal_rdd;
			}
		});
		System.out.println("=============>>>>>>>>>>>>>>>>>>  start ==>>> " + System.currentTimeMillis());
		trp_rdd.print();
		System.out.println("=============>>>>>>>>>>>>>>>>>>  end ===>>> "  + System.currentTimeMillis());
		sc.start();
		sc.awaitTermination();
		sc.close();
		
		
	}
}
