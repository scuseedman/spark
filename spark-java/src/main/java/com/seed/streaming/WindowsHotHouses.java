/** 
 * Project Name:spark-java 
 * File Name:WindowsHotHouses.java 
 * Package Name:com.seed.streaming 
 * Date:2018年1月4日下午3:43:09 
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import kafka.common.TopicAndPartition;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.HasOffsetRanges;
import org.apache.spark.streaming.kafka.KafkaCluster;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.streaming.kafka.OffsetRange;

import scala.Predef;
import scala.Tuple2;
import scala.collection.JavaConversions;

import com.seed.config.Global;

/** 
 * ClassName:WindowsHotHouses
 * Date:     2018年1月4日 下午3:43:09 
 * DESC:	滑动窗口计算一段时间内的热词 _seed
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class WindowsHotHouses implements Serializable{
	private static final long serialVersionUID = 1L;
	private static Map<String,String> kafkaParam = new HashMap<String,String>();
	private static Logger log = Logger.getLogger(WindowsHotHouses.class);
	private static KafkaCluster kafkaCluster = null;
	private static scala.collection.immutable.Set<String> immutableTopics;
	private static Broadcast<Map<String, String>> kafkaParamBroadcast = null;
	public static void main(String[] args) {
		String masterName = System.getProperty("os.name").toLowerCase().contains("windows")?"local[2]":"yarn-cluster";
		new WindowsHotHouses().countWindowsHots(masterName);
		
	}
	private static void initKafkaParams() {
        kafkaParam.put("metadata.broker.list", Global.getConfVal("BROKER_LIST"));
        kafkaParam.put("zookeeper.connect", Global.getConfVal("ZK_CONNECT"));
        kafkaParam.put("auto.offset.reset", Global.getConfVal("AUTO_OFFSET_RESET"));
        kafkaParam.put("group.id", Global.getConfVal("GROUP_ID"));
    }
	private static KafkaCluster initKafkaCluster() {
        log.warn("transform java Map to scala immutable.map");
        // transform java Map to scala immutable.map
        scala.collection.mutable.Map<String, String> testMap = JavaConversions.mapAsScalaMap(kafkaParam);
        scala.collection.immutable.Map<String, String> scalaKafkaParam = testMap
                .toMap(new Predef.$less$colon$less<Tuple2<String, String>, Tuple2<String, String>>() {
                    private static final long serialVersionUID = 1L;
                    public Tuple2<String, String> apply(Tuple2<String, String> tuple) {
                        return tuple;
                    }
                });
 
        // init KafkaCluster
        log.warn("Init KafkaCluster");
        return new KafkaCluster(scalaKafkaParam);
    }
	private static Map<TopicAndPartition,Long> initConsumerOffset(String topic){
		Set<String> topicSet = new HashSet<String>();
		topicSet.add(topic);
		scala.collection.mutable.Set<String> mutableTopics  = JavaConversions.asScalaSet(topicSet);
		immutableTopics = mutableTopics.toSet();
		if(null == kafkaCluster){
			System.out.println(" ===>>> kafkaCluster is null !!");
		}
		scala.collection.immutable.Set<TopicAndPartition> topicAndPartitionSet  = kafkaCluster.getPartitions(immutableTopics).right().get();
		Map<TopicAndPartition,Long> consumerOffsetsLong = new HashMap<TopicAndPartition,Long>();
		if(kafkaCluster.getConsumerOffsetMetadata(kafkaParam.get("group.id"), topicAndPartitionSet).isLeft()){
			log.warn(" ===>>> 没有保存的offset .各个partitions的offset 都将会设置为 0 ");
			Set<TopicAndPartition> topicAndPartitionSet1 = JavaConversions.setAsJavaSet(topicAndPartitionSet);
            for (TopicAndPartition topicAndPartition : topicAndPartitionSet1) {
                consumerOffsetsLong.put(topicAndPartition, 0L);
            }
		}else{
			log.warn("已经存在offset ,将会使用存在的offset !!");
			scala.collection.immutable.Map<TopicAndPartition, Object> consumerOffsetsTemp = 
					kafkaCluster.getConsumerOffsets(kafkaParam.get("group.id"), topicAndPartitionSet).right().get();
			Map<TopicAndPartition, Object> consumerOffsets = JavaConversions.mapAsJavaMap(consumerOffsetsTemp);
            Set<TopicAndPartition> topicAndPartitionSet1 = JavaConversions.setAsJavaSet(topicAndPartitionSet);
            log.warn("put data into kafka offset long " );
            for (TopicAndPartition topicAndPartition : topicAndPartitionSet1) {
                Long offset = (Long) consumerOffsets.get(topicAndPartition);
                consumerOffsetsLong.put(topicAndPartition, offset);
            }
		}
		return consumerOffsetsLong;
	}
	private static JavaInputDStream<String> createKafkaDStream(JavaStreamingContext sc,Map<TopicAndPartition, Long> consumerOffsetsLong) {
		log.warn("Create KafkaDriectStream with Offset");
		JavaInputDStream<String>  message = KafkaUtils.createDirectStream(sc,
				String.class, 
				String.class, 
				StringDecoder.class,
				StringDecoder.class,
				String.class,
				kafkaParamBroadcast.getValue(), 
				consumerOffsetsLong,
				new Function<MessageAndMetadata<String, String>, String>() {
            private static final long serialVersionUID = 1L;
            public String call(MessageAndMetadata<String, String> v1) throws Exception {
                return v1.message();
            }
        });
		
		return message;
	}
	private JavaDStream<String> getAndUpdateKafkaOffset(
			JavaInputDStream<String> message,
			final AtomicReference<OffsetRange[]> offsetRanges) {
		JavaDStream<String> javaDStream = message.transform(new Function<JavaRDD<String>, JavaRDD<String>>() {
			private static final long serialVersionUID = 1L;
			public JavaRDD<String> call(JavaRDD<String> message) throws Exception {
				OffsetRange[] offsets = ((HasOffsetRanges)message.rdd()).offsetRanges();
				offsetRanges.set(offsets);
//				for(int i = 0 ; i < offsets.length ; i ++){
//					log.warn("topic : {" +offsets[i].topic() + "}, partitions: {" +offsets[i].partition() 
//							+" }, fromoffset: {" + offsets[i].fromOffset() + "}, untiloffset: {" + offsets[i].untilOffset() + " }"
//                          );
//				}
				return message;
			}
		});
		log.warn(" ===>>> foreachRDD");
		javaDStream.foreachRDD(new VoidFunction<JavaRDD<String>>() {
			private static final long serialVersionUID = 1L;
			public void call(JavaRDD<String> rdd) throws Exception {
				if(rdd.isEmpty()){
					System.out.println(" ===>>> empty RDD !!!");
					return ;
				}
				for(OffsetRange offset : offsetRanges.get()){
					// 封装topic.partition 与 offset对应关系 java Map
					TopicAndPartition topicAndPartition = new TopicAndPartition(offset.topic(), offset.partition());
                    Map<TopicAndPartition, Object> topicAndPartitionObjectMap = new HashMap<TopicAndPartition, Object>();
                    topicAndPartitionObjectMap.put(topicAndPartition, offset.untilOffset());
//                    log.warn("Topic: " + offset.topic() + " partitions: " + offset.partition() + " offset : " + offset.untilOffset());
                    // 转换java map to scala immutable.map
                    scala.collection.mutable.Map<TopicAndPartition, Object> testMap = JavaConversions
                            .mapAsScalaMap(topicAndPartitionObjectMap);
                    scala.collection.immutable.Map<TopicAndPartition, Object> scalatopicAndPartitionObjectMap = testMap
                            .toMap(new Predef.$less$colon$less<Tuple2<TopicAndPartition, Object>, Tuple2<TopicAndPartition, Object>>() {
                                private static final long serialVersionUID = 1L;
 
                                @Override
                                public Tuple2<TopicAndPartition, Object> apply(Tuple2<TopicAndPartition, Object> v1) {
                                    return v1;
                                }
                            });
                 // 更新offset到kafkaCluster
                    kafkaCluster.setConsumerOffsets(kafkaParamBroadcast.getValue().get("group.id"),
                            scalatopicAndPartitionObjectMap);
				}
			}
		});
		return javaDStream;
	}
	public void countWindowsHots(String masterName){
		initKafkaParams() ;//初始化kafka参数
		kafkaCluster = initKafkaCluster();
		SparkConf sparkConf = new SparkConf().setMaster(masterName).setAppName("spark in windows");
		sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
		sparkConf.set("spark.streaming.stopGracefullyOnShutdown","true");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.setLogLevel("WARN");
		JavaStreamingContext sc = new JavaStreamingContext(jsc,new Duration(10000));
		log.warn("init comsumer offset !!");
		Map<TopicAndPartition, Long> consumerOffsetsLong = initConsumerOffset(Global.getConfVal("KAFKA_TOPIC"));
		kafkaParamBroadcast = sc.sparkContext().broadcast(kafkaParam);
		JavaInputDStream<String> message = createKafkaDStream(sc, consumerOffsetsLong);
		final AtomicReference<OffsetRange[]> offsetRanges = new AtomicReference<OffsetRange[]>();
		JavaDStream<String> javaDStream = getAndUpdateKafkaOffset(message,offsetRanges);
		
		//开始处理逻辑
		JavaDStream<String>  words = javaDStream.flatMap(new FlatMapFunction<String, String>() {
			private static final long serialVersionUID = 1L;
			public Iterable<String> call(String line) throws Exception {
				return Arrays.asList(line.split(","));//字段分割符
			}
		});
		 JavaPairDStream<String, Integer> p_words = words.mapToPair(new PairFunction<String, String, Integer>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, Integer> call(String word) throws Exception {
				return new Tuple2<String,Integer>(word,1);
			}
		});
		 JavaPairDStream<String, Integer>  w_words = p_words.reduceByKeyAndWindow(new Function2<Integer, Integer, Integer>() {
			private static final long serialVersionUID = 1L;
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1 + v2;
			}
		}, Durations.seconds(60),Durations.seconds(10));
		 //每10s处理前面60s的数据；该60s的数据将会一个RDD，对这个rdd进行操作
		 JavaPairDStream<String, Integer>  windows = w_words.transformToPair(new Function<JavaPairRDD<String,Integer>, JavaPairRDD<String,Integer>>() {
			private static final long serialVersionUID = 1L;
			public JavaPairRDD<String, Integer> call(
					JavaPairRDD<String, Integer> w_words) throws Exception {
				//本来是(word,1)的结果，对结果进行反转，也就是将会变成(1,word)的结构
				JavaPairRDD<Integer, String>  r_words = w_words.mapToPair(new PairFunction<Tuple2<String,Integer>, Integer, String>() {
					private static final long serialVersionUID = 1L;
					public Tuple2<Integer, String> call(
							Tuple2<String, Integer> w_word) throws Exception {
						return new Tuple2<Integer,String>(w_word._2,w_word._1);
					}
				});
				JavaPairRDD<Integer, String>  s_words = r_words.sortByKey(false);
				//再次执行反转操作，变成正常的wordcount格式 
				JavaPairRDD<String, Integer>  n_words = s_words.mapToPair(new PairFunction<Tuple2<Integer,String>, String, Integer>() {
					private static final long serialVersionUID = 1L;
					public Tuple2<String, Integer> call(
							Tuple2<Integer, String> tuple) throws Exception {
						return new Tuple2<String,Integer>(tuple._2,tuple._1);
					}
				});
				List<Tuple2<String, Integer>>  lists = n_words.take(3);//取出前10的热词
				for(Tuple2<String,Integer> tuple:lists){
					System.out.println(tuple._1 + " ---===>>> " + tuple._2);
				}
				return w_words;
			}
		});
		// 这个无关紧要，只是为了触发job的执行，所以必须有output操作
		 System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ====================  start");
		 windows.print();
		 System.out.println(" ==================== <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  end");
		 
		sc.start();
		sc.awaitTermination();
		sc.close();
	}
	
}
