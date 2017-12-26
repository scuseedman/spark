package com.seed.transformation;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kafka.serializer.StringDecoder;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import scala.Tuple2;

/**
 * 使用direct 方式进行消费kafka数据; 消费的数据可以记录到kafka的offset。过往的历史消息并没有被消费出来
 * @author lwd; 
 *
 */
public class KafkaDirectStreaming {
	public static void main(String[] args) {
		SparkConf sparkConf = new SparkConf().setAppName("sparkDirect").setMaster("local[2]");
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);//用来设置日志级别
		ctx.setLogLevel("WARN");
		JavaStreamingContext jsc = new JavaStreamingContext(ctx,new Duration(5000));
		
		//创建kafka参数map
		Map<String,String> params = new HashMap<String,String>();//kafka参数列表
		params.put("metadata.broker.list", args[0]);
		params.put("group.id", "46");//设置group id 也没有产生效果...... _seed
		Set<String> topics = new HashSet<String>();//需要读取的topic的集合
		topics.add("cs_finance");
		JavaPairInputDStream<String,String> lines = KafkaUtils.createDirectStream(
				jsc,
				String.class,
				String.class,
				StringDecoder.class,
				StringDecoder.class, 
				params,
				topics);
		//执行wordcount操作
		JavaDStream<String> words = lines.flatMap(new FlatMapFunction<Tuple2<String,String>, String>() {
			private static final long serialVersionUID = 1L;
			public Iterable<String> call(Tuple2<String, String> line)
					throws Exception {
				return Arrays.asList(line._2.split(" "));//切分这个元组的第二个元素才是获取得到的kafka中的数据
			}
		});
		JavaPairDStream<String, Integer> words1 = words.mapToPair(new PairFunction<String, String, Integer>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, Integer> call(String word) throws Exception {
				return new Tuple2<String,Integer>(word,1);
			}
		});
		
		JavaPairDStream<String,Integer> result = words1.reduceByKey(new Function2<Integer, Integer, Integer>() {
			private static final long serialVersionUID = 1L;
			public Integer call(Integer count1, Integer count2) throws Exception {
				return count1 + count2;
			}
		});
		result.print();
				
		jsc.start();
		jsc.awaitTermination();
		jsc.close();
	}
}
