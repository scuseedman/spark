/** 
 * Project Name:spark-java 
 * File Name:KafkaOffsetExample.java 
 * Package Name:com.seed.utils 
 * Date:2017年12月25日下午5:17:22 
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
/** 
 * ClassName:KafkaOffsetExample
 * Date:     2017年12月25日 下午5:17:22 
 * DESC:	数据从kafka上次读取之后的位置开始处理。结果可以正确执行。数据put到redis中 。_seed
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

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
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.HasOffsetRanges;
import org.apache.spark.streaming.kafka.KafkaCluster;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.streaming.kafka.OffsetRange;

import com.seed.config.Global;
import com.seed.service.impl.RedisServiceImpl;

import kafka.common.TopicAndPartition;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;
import scala.Predef;
import scala.Tuple2;
import scala.collection.JavaConversions;
//import lombok.extern.slf4j.*;
 
//@Slf4j
public class SparkStreamingDemo3 {
    private static KafkaCluster kafkaCluster = null;
    private static HashMap<String, String> kafkaParam = new HashMap<String, String>();
    private static Broadcast<HashMap<String, String>> kafkaParamBroadcast = null;
    private static scala.collection.immutable.Set<String> immutableTopics = null;
    private static Logger log = Logger.getLogger(SparkStreamingDemo3.class);
    private static final String redisHost = Global.getConfVal("REDISHOST");
 
    /** * Create the Kafka Stream Directly With Offset in ZK * * @param jssc * SparkStreamContext * @param consumerOffsetsLong * Save the Offset of Kafka Topic * @return */
    private static JavaInputDStream<String> createKafkaDStream(JavaStreamingContext jssc,
            Map<TopicAndPartition, Long> consumerOffsetsLong) {
        SparkStreamingDemo3.log.warn("Create KafkaDriectStream with Offset");
        JavaInputDStream<String> message = KafkaUtils.createDirectStream(jssc, String.class, String.class,
                StringDecoder.class, StringDecoder.class, String.class, kafkaParamBroadcast.getValue(),
                consumerOffsetsLong, new Function<MessageAndMetadata<String, String>, String>() {
                    private static final long serialVersionUID = 1L;
                    public String call(MessageAndMetadata<String, String> v1) throws Exception {
                        return v1.message();
                    }
                });
        return message;
    }
 
    private static Map<TopicAndPartition, Long> initConsumerOffset(String topic) {
        Set<String> topicSet = new HashSet<String>();
        topicSet.add(topic);
        scala.collection.mutable.Set<String> mutableTopics = JavaConversions.asScalaSet(topicSet);
        immutableTopics = mutableTopics.toSet();
        if(null == kafkaCluster){
        	System.out.println(  "=====>>>>>  kafkaCluster is null !!!!");
        }
        scala.collection.immutable.Set<TopicAndPartition> topicAndPartitionSet = kafkaCluster
                .getPartitions(immutableTopics).right().get();
         
        // kafka direct stream 初始化时使用的offset数据
        Map<TopicAndPartition, Long> consumerOffsetsLong = new HashMap<TopicAndPartition, Long>();
        if (kafkaCluster.getConsumerOffsets(kafkaParam.get("group.id"), topicAndPartitionSet).isLeft()) {
            SparkStreamingDemo3.log.warn("没有保存offset, 各个partition offset 默认为0");
            Set<TopicAndPartition> topicAndPartitionSet1 = JavaConversions.setAsJavaSet(topicAndPartitionSet);
            for (TopicAndPartition topicAndPartition : topicAndPartitionSet1) {
                consumerOffsetsLong.put(topicAndPartition, 0L);
            }
        }
        else {
            SparkStreamingDemo3.log.warn("offset已存在, 使用保存的offset");
            scala.collection.immutable.Map<TopicAndPartition, Object> consumerOffsetsTemp = kafkaCluster
                    .getConsumerOffsets(kafkaParam.get("group.id"), topicAndPartitionSet).right().get();
 
            Map<TopicAndPartition, Object> consumerOffsets = JavaConversions.mapAsJavaMap(consumerOffsetsTemp);
            Set<TopicAndPartition> topicAndPartitionSet1 = JavaConversions.setAsJavaSet(topicAndPartitionSet);
 
            SparkStreamingDemo3.log.warn("put data in consumerOffsetsLong");
            for (TopicAndPartition topicAndPartition : topicAndPartitionSet1) {
                Long offset = (Long) consumerOffsets.get(topicAndPartition);
                consumerOffsetsLong.put(topicAndPartition, offset);
            }
        }
        return consumerOffsetsLong;
    }
     
    private static JavaDStream<String> getAndUpdateKafkaOffset(JavaInputDStream<String> message,
              final AtomicReference<OffsetRange[]> offsetRanges) {
        JavaDStream<String> javaDStream = message.transform(new Function<JavaRDD<String>, JavaRDD<String>>() {
            private static final long serialVersionUID = 1L;
            public JavaRDD<String> call(JavaRDD<String> rdd) throws Exception {
                OffsetRange[] offsets = ((HasOffsetRanges) rdd.rdd()).offsetRanges();
                offsetRanges.set(offsets);
                //代码块报错。原因待查找 _seed
//                for (int i = 0; i < offsets.length; i++)
//                    KafkaOffsetExample.log.warn("topic : {}, partitions: {}, fromoffset: {}, untiloffset: {}",
//                            offsets[i].topic(), offsets[i].partition(), offsets[i].fromOffset(),
//                            offsets[i].untilOffset());
                return rdd;
            }
        });
        SparkStreamingDemo3.log.warn("foreachRDD");
        // output
        javaDStream.foreachRDD(new VoidFunction<JavaRDD<String>>() {
            private static final long serialVersionUID = 1L;
 
            public void call(JavaRDD<String> rdd) throws Exception {
                if (rdd.isEmpty()) {
                    SparkStreamingDemo3.log.warn("Empty RDD");
                    return;
                }
                for (OffsetRange o : offsetRanges.get()) {
                    // 封装topic.partition 与 offset对应关系 java Map
                    TopicAndPartition topicAndPartition = new TopicAndPartition(o.topic(), o.partition());
                    Map<TopicAndPartition, Object> topicAndPartitionObjectMap = new HashMap<TopicAndPartition, Object>();
                    topicAndPartitionObjectMap.put(topicAndPartition, o.untilOffset());
 
                    SparkStreamingDemo3.log.warn(
                            "Topic: " + o.topic() + " partitions: " + o.partition() + " offset : " + o.untilOffset());
 
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
     
    private static void initKafkaParams() {
        kafkaParam.put("metadata.broker.list", Global.getConfVal("BROKER_LIST"));
        kafkaParam.put("zookeeper.connect", Global.getConfVal("ZK_CONNECT"));
        kafkaParam.put("auto.offset.reset", Global.getConfVal("AUTO_OFFSET_RESET"));
        kafkaParam.put("group.id", Global.getConfVal("GROUP_ID"));
    }
     
    private static KafkaCluster initKafkaCluster() {
        SparkStreamingDemo3.log.warn("transform java Map to scala immutable.map");
        // transform java Map to scala immutable.map
        scala.collection.mutable.Map<String, String> testMap = JavaConversions.mapAsScalaMap(kafkaParam);
        scala.collection.immutable.Map<String, String> scalaKafkaParam = testMap
                .toMap(new Predef.$less$colon$less<Tuple2<String, String>, Tuple2<String, String>>() {
                    private static final long serialVersionUID = 1L;
                    public Tuple2<String, String> apply(Tuple2<String, String> arg0) {
                        return arg0;
                    }
                });
 
        // init KafkaCluster
        SparkStreamingDemo3.log.warn("Init KafkaCluster");
        return new KafkaCluster(scalaKafkaParam);
    }
     
    @SuppressWarnings("deprecation")
	public static void run(String masterName) {
        initKafkaParams();
        kafkaCluster = initKafkaCluster();
 
        SparkConf sparkConf = new SparkConf().setMaster(masterName).setAppName("sparkStreaming");
        sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");//设置spark 实体类序列化的类
        JavaSparkContext jsc =  new JavaSparkContext(sparkConf);
        jsc.setLogLevel("WARN");
        JavaStreamingContext jssc = new JavaStreamingContext(jsc, new Duration(5000));
        
        // 得到rdd各个分区对应的offset, 并保存在offsetRanges中
        SparkStreamingDemo3.log.warn("initConsumer Offset");
        Map<TopicAndPartition, Long> consumerOffsetsLong = initConsumerOffset(Global.getConfVal("KAFKA_TOPIC"));
        kafkaParamBroadcast = jssc.sparkContext().broadcast(kafkaParam);
         
        JavaInputDStream<String> message = createKafkaDStream(jssc, consumerOffsetsLong);
        final AtomicReference<OffsetRange[]> offsetRanges = new AtomicReference<OffsetRange[]>();
        JavaDStream<String> javaDStream = getAndUpdateKafkaOffset(message, offsetRanges);
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
        JavaPairDStream<String, Integer> res = word_kv.reduceByKey(new Function2<Integer, Integer, Integer>() {
			private static final long serialVersionUID = 1L;
			public Integer call(Integer count1, Integer count2) throws Exception {
				return count1 + count2;
			}
		});
        
         res.foreachRDD(new Function<JavaPairRDD<String,Integer>, Void>() {
 			private static final long serialVersionUID = 1L;
 			public Void call(JavaPairRDD<String, Integer> rdd) throws Exception {
 				rdd.foreach(new VoidFunction<Tuple2<String,Integer>>() {
 					private static final long serialVersionUID = 1L;
 					public void call(Tuple2<String, Integer> wordcount) throws Exception {
 						System.out.println(wordcount._1 + " ===>>> " + wordcount._2);
 						SparkStreamingDemo3.log.warn( " ========>>>>>>>  redisHost is :  " + redisHost);
 						new RedisServiceImpl(redisHost).putRes2Redis(wordcount);
 					}
 				});
 				return null;
 			}
 		});
        jssc.start();
        jssc.awaitTermination();
    }
     
    public static void main(String[] args) throws Exception {
//    	String path = "";
    	String masterName = "";
    	System.out.println(" ======>>>>> 程序启动，请确保配置文件存在于 ==>> /data/formax_data/data_test/shell-scripts/config.properties ...");
    	System.out.println("===> " + Global.getConfVal("REDISHOST"));
    	System.out.println("===> " + Global.getConfVal("KAFKA_TOPIC"));
    	SparkStreamingDemo3.log.warn("======>>>>>> " + Global.getConfVal("REDISHOST"));
    	if(System.getProperty("os.name").toLowerCase().contains("windows")){
//    		path = "E:\\formax_workspace\\spark\\spark-java\\src\\main\\resources\\config\\config.properties";
    		masterName = "local[2]";
    	}else if(System.getProperty("os.name").toLowerCase().contains("linux")){
//    		path = "file:/data/formax_data/data_test/shell-scripts/config.properties";
    		masterName = "yarn-cluster";
    	}
        SparkStreamingDemo3.run(masterName);
    }
}
