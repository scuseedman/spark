/** 
 * Project Name:spark-java 
 * File Name:UpdateStateByKeyZhangfei.java 
 * Package Name:com.seed.streaming 
 * Date:2018年1月5日下午3:22:45 
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

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import kafka.common.TopicAndPartition;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaCluster;
import org.apache.spark.streaming.kafka.OffsetRange;

import scala.Tuple2;

import com.google.common.base.Optional;
import com.seed.config.Global;
import com.seed.utils.OffSetUtil;

/** 
 * ClassName:UpdateStateByKeyZhangfei
 * Date:     2018年1月5日 下午3:22:45 
 * DESC:	
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class UpdateStateByKey implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Integer INTERVAL = 10000;
	private Map<String,String> kafkaParam = null;
	 private static KafkaCluster kafkaCluster = null;
	 private static Broadcast<Map<String, String>> kafkaParamBroadcast = null;
	 
	private void updateStateByKeyFromKafka(String masterName) {
		SparkConf sparkConf = new SparkConf().setAppName("update").setMaster(masterName);
		sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");//设置spark 实体类序列化的类
		sparkConf.set("spark.streaming.stopGracefullyOnShutdown","true");//设置钩子，使之可以优雅的关停掉这个sparkstreaming程序
		JavaSparkContext jsc =  new JavaSparkContext(sparkConf);
		jsc.setLogLevel("WARN");
		JavaStreamingContext sc = new JavaStreamingContext(jsc,new Duration(INTERVAL));
		// 第一点，如果要使用updateStateByKey算子，就必须设置一个checkpoint目录，开启checkpoint机制
				// 这样的话才能把每个key对应的state除了在内存中有，那么是不是也要checkpoint一份
				// 因为你要长期保存一份key的state的话，那么spark streaming是要求必须用checkpoint的，以便于在
				// 内存数据丢失的时候，可以从checkpoint中恢复数据
				
				// 开启checkpoint机制，很简单，只要调用jssc的checkpoint()方法，设置一个hdfs目录即可
		sc.checkpoint("usbCheckPoint");
		kafkaParam = OffSetUtil.initKafkaParams();//初始化kafka连接参数
		kafkaCluster = OffSetUtil.initKafkaCluster(kafkaParam);
		Map<TopicAndPartition, Long> consumerOffsetsLong = OffSetUtil.initConsumerOffset(Global.getConfVal("KAFKA_TOPIC"),kafkaCluster,kafkaParam);
		kafkaParamBroadcast = sc.sparkContext().broadcast(kafkaParam);
		JavaInputDStream<String> message = OffSetUtil.createKafkaDStream(sc, consumerOffsetsLong,kafkaParamBroadcast);
		final AtomicReference<OffsetRange[]> offsetRanges = new AtomicReference<OffsetRange[]>();
		JavaDStream<String> javaDStream = OffSetUtil.getAndUpdateKafkaOffset(message, offsetRanges,kafkaCluster,kafkaParamBroadcast);
		//开始处理逻辑 _seed
        JavaDStream<String> words = javaDStream.flatMap(new FlatMapFunction<String, String>() {//flatMap
			private static final long serialVersionUID = 1L;
			public Iterable<String> call(String line) throws Exception {
				return Arrays.asList(line.split(" "));
			}
		});
        JavaPairDStream<String, Integer> word_kv = words.mapToPair(new PairFunction<String, String, Integer>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, Integer> call(String word) throws Exception {
				return new Tuple2<String,Integer>(word,1);
			}
		});
        // 到了这里，就不一样了，之前的话，是不是直接就是pairs.reduceByKey
        // 然后，就可以得到每个时间段的batch对应的RDD，计算出来的单词计数
        // 然后，可以打印出那个时间段的单词计数
        // 但是，有个问题，你如果要统计每个单词的全局的计数呢？
        // 就是说，统计出来，从程序启动开始，到现在为止，一个单词出现的次数，那么就之前的方式就不好实现
        // 就必须基于redis这种缓存，或者是mysql这种db，来实现累加
        
        // 但是，我们的updateStateByKey，就可以实现直接通过Spark维护一份每个单词的全局的统计次数
        JavaPairDStream<String,Integer> usb_words = word_kv.updateStateByKey(new Function2<List<Integer>, Optional<Integer>, Optional<Integer>>() {
			private static final long serialVersionUID = 1L;
			// 这里的Optional，相当于Scala中的样例类，就是Option，可以这么理解
			// 它代表了一个值的存在状态，可能存在，也可能不存在
			public Optional<Integer> call(List<Integer> values,
					Optional<Integer> state) throws Exception {
				// 这里两个参数
				// 实际上，对于每个单词，每次batch计算的时候，都会调用这个函数
				// 第一个参数，values，相当于是这个batch中，这个key的新的值，可能有多个吧
				// 比如说一个hello，可能有2个1，(hello, 1) (hello, 1)，那么传入的是(1,1)
				// 第二个参数，就是指的是这个key之前的状态，state，其中泛型的类型是你自己指定的
				// 首先定义一个全局的单词计数
				Integer newValue = 0;
				
				// 其次，判断，state是否存在，如果不存在，说明是一个key第一次出现
				// 如果存在，说明这个key之前已经统计过全局的次数了
				if(state.isPresent()) {
					newValue = state.get();
				}
				
				// 接着，将本次新出现的值，都累加到newValue上去，就是一个key目前的全局的统计
				// 次数
				for(Integer value : values) {
					newValue += value;
				}
				
				return Optional.of(newValue);  
			}
		});
     // 到这里为止，相当于是，每个batch过来是，计算到pairs DStream，就会执行全局的updateStateByKey
     		// 算子，updateStateByKey返回的JavaPairDStream，其实就代表了每个key的全局的计数
     		// 打印出来
//        适当的操作方式应该是持久化一份 _seed
        usb_words.print();
        
		sc.start();
		sc.awaitTermination();
		sc.stop();
		
	}
	
	public static void main(String[] args) {
		String masterName = System.getProperty("os.name").toLowerCase().contains("windows")?"local[2]":"yarn-cluster";
		new UpdateStateByKey().updateStateByKeyFromKafka(masterName);
	}

}
