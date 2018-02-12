/** 
 * Project Name:spark-java 
 * File Name:OffSetUtil.java 
 * Package Name:com.seed.utils 
 * Date:2018年1月5日下午3:18:42 
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
package com.seed.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import kafka.common.TopicAndPartition;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;

import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
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
 * ClassName:OffSetUtil
 * Date:     2018年1月5日 下午3:18:42 
 * DESC:	维护spark streaming 从kafka读数时的offset 工具类
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class OffSetUtil {
	private static scala.collection.immutable.Set<String> immutableTopics = null;
	private static Logger log = Logger.getLogger(OffSetUtil.class);
	public static Map<String, String> initKafkaParams() {
		Map<String, String> kafkaParam = new HashMap<String, String>();
		kafkaParam.put("metadata.broker.list", Global.getConfVal("BROKER_LIST"));
        kafkaParam.put("zookeeper.connect", Global.getConfVal("ZK_CONNECT"));
        kafkaParam.put("auto.offset.reset", Global.getConfVal("AUTO_OFFSET_RESET"));
        kafkaParam.put("group.id", Global.getConfVal("GROUP_ID"));
		return kafkaParam;
	}

	public static KafkaCluster initKafkaCluster(Map<String,String> kafkaParam) {
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
        return new KafkaCluster(scalaKafkaParam);	
	}

	public static Map<TopicAndPartition, Long> initConsumerOffset(String topic,KafkaCluster kafkaCluster
			,Map<String,String> kafkaParam) {
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
            log.warn("没有保存offset, 各个partition offset 默认为0");
            Set<TopicAndPartition> topicAndPartitionSet1 = JavaConversions.setAsJavaSet(topicAndPartitionSet);
            for (TopicAndPartition topicAndPartition : topicAndPartitionSet1) {
                consumerOffsetsLong.put(topicAndPartition, 0L);
            }
        }
        else {
            log.warn("offset已存在, 使用保存的offset");
            scala.collection.immutable.Map<TopicAndPartition, Object> consumerOffsetsTemp = kafkaCluster
                    .getConsumerOffsets(kafkaParam.get("group.id"), topicAndPartitionSet).right().get();
 
            Map<TopicAndPartition, Object> consumerOffsets = JavaConversions.mapAsJavaMap(consumerOffsetsTemp);
            Set<TopicAndPartition> topicAndPartitionSet1 = JavaConversions.setAsJavaSet(topicAndPartitionSet);
 
            log.warn("put data in consumerOffsetsLong");
            for (TopicAndPartition topicAndPartition : topicAndPartitionSet1) {
                Long offset = (Long) consumerOffsets.get(topicAndPartition);
                consumerOffsetsLong.put(topicAndPartition, offset);
            }
        }
        return consumerOffsetsLong;
	}
	/** 
	 * 从kafka获取信息
	 * @param sc
	 * @param consumerOffsetsLong
	 * @param kafkaParamBroadcast
	 * @return
	 */

	public static JavaInputDStream<String> createKafkaDStream(
			JavaStreamingContext sc,
			Map<TopicAndPartition, Long> consumerOffsetsLong,
			Broadcast<Map<String, String>> kafkaParamBroadcast) {
		log.warn("Create KafkaDriectStream with Offset");
        JavaInputDStream<String> message = KafkaUtils.createDirectStream(sc, String.class, String.class,
                StringDecoder.class, StringDecoder.class, String.class, kafkaParamBroadcast.getValue(),
                consumerOffsetsLong, new Function<MessageAndMetadata<String, String>, String>() {
                    private static final long serialVersionUID = 1L;
                    public String call(MessageAndMetadata<String, String> v1) throws Exception {
                        return v1.message();
                    }
                });
        return message;
	}

	public static JavaDStream<String> getAndUpdateKafkaOffset(
			JavaInputDStream<String> message,
			final AtomicReference<OffsetRange[]> offsetRanges,
			final KafkaCluster kafkaCluster,
			final Broadcast<Map<String, String>> kafkaParamBroadcast) {
		JavaDStream<String> javaDStream = message.transform(new Function<JavaRDD<String>, JavaRDD<String>>() {
            private static final long serialVersionUID = 1L;
            public JavaRDD<String> call(JavaRDD<String> rdd) throws Exception {
                OffsetRange[] offsets = ((HasOffsetRanges) rdd.rdd()).offsetRanges();
                offsetRanges.set(offsets);
                //代码已经改正 _seed
//                for (int i = 0; i < offsets.length; i++)
//                    log.warn("topic : {" +offsets[i].topic() + "}, partitions: {" + offsets[i].partition() 
//                    		+ "}, fromoffset: {" + offsets[i].fromOffset() + "}, untiloffset: {" + offsets[i].untilOffset() + "}"
//                            );
                return rdd;
            }
        });
        log.warn("foreachRDD");
        // output
        javaDStream.foreachRDD(new VoidFunction<JavaRDD<String>>() {
            private static final long serialVersionUID = 1L;
 
            public void call(JavaRDD<String> rdd) throws Exception {
                if (rdd.isEmpty()) {
                    log.warn("Empty RDD");
                    return;
                }
                for (OffsetRange o : offsetRanges.get()) {
                    // 封装topic.partition 与 offset对应关系 java Map
                    TopicAndPartition topicAndPartition = new TopicAndPartition(o.topic(), o.partition());
                    Map<TopicAndPartition, Object> topicAndPartitionObjectMap = new HashMap<TopicAndPartition, Object>();
                    topicAndPartitionObjectMap.put(topicAndPartition, o.untilOffset());
 
                    log.info(
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
	
}
