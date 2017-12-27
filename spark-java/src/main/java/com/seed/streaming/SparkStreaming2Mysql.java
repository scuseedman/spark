/** 
 * Project Name:spark-java 
 * File Name:SparkStreaming2Hbase.java 
 * Date:2017年12月8日上午11:18:21 
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
 * ClassName:SparkStreaming2Hbase
 * Date:     2017年12月8日 上午11:18:21 
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */



import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Time;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;

import scala.Tuple2;

import com.seed.entity.WordCountEntity;
import com.seed.service.WordCountService;

public class SparkStreaming2Mysql {
  private static final Pattern SPACE = Pattern.compile(" ");

//  private static ApplicationContext app;
//  static {
//      app = new ClassPathXmlApplicationContext(new String[] { "classpath:spring/spring.xml", "classpath:spring/spring-mybatis.xml" });
//  }
//  private static WordCountService wcService = app.getBean(WordCountService.class);
  @SuppressWarnings("deprecation")
public static void main(String[] args) {
//    if (args.length == 0) {
//      System.err
//          .println("Usage: SparkStreamingFromFlumeToHBaseWindowingExample {master} {host} {port} {table} {columnFamily} {windowInSeconds} {slideInSeconds");
//      System.exit(1);
//    }

    // String master = args[0];
    // String host = args[1];
    // int port = Integer.parseInt(args[2]);
    // int windowInSeconds = 3;// Integer.parseInt(args[5]);
    // int slideInSeconds = 1;// Integer.parseInt(args[5]);

    String zkQuorum = args[0];
    String group = "47";
    String topicss = "cs_finance";
    String numThread = "2";

//    Duration batchInterval = new Duration(5000);
    
    // Duration windowInterval = new Duration(windowInSeconds * 1000);
    // Duration slideInterval = new Duration(slideInSeconds * 1000);

    SparkConf sparkConf = new SparkConf().setAppName("hello spark");
    JavaSparkContext sc = new JavaSparkContext(sparkConf);
    sc.setLogLevel("WARN");
    JavaStreamingContext jssc =
        new JavaStreamingContext(sc, new Duration(2000));

    // JavaDStream<SparkFlumeEvent> flumeStream = sc.flumeStream(host, port);

    int numThreads = Integer.parseInt(numThread);
    Map<String, Integer> topicMap = new HashMap<String, Integer>();
    String[] topics = topicss.split(",");
    for (String topic : topics) {
      topicMap.put(topic, numThreads);
    }

    JavaPairReceiverInputDStream<String, String> messages =
        KafkaUtils.createStream(jssc, zkQuorum, group, topicMap);

    JavaDStream<String> lines =
        messages.map(new Function<Tuple2<String, String>, String>() {
			private static final long serialVersionUID = 1L;

		@Override
          public String call(Tuple2<String, String> tuple2) {
            return tuple2._2();
          }
        });

    JavaPairDStream<String, Integer> lastCounts =
        messages.map(new Function<Tuple2<String, String>, String>() {
			private static final long serialVersionUID = 1L;

		@Override
          public String call(Tuple2<String, String> tuple2) {
            return tuple2._2();
          }
        }).flatMap(new FlatMapFunction<String, String>() {
			private static final long serialVersionUID = 1L;
		@Override
          public Iterable<String> call(String x) {
            return Arrays.asList(SPACE.split(x));
          }
        }).mapToPair(new PairFunction<String, String, Integer>() {
			private static final long serialVersionUID = 1L;
		@Override
          public Tuple2<String, Integer> call(String s) {
            return new Tuple2<String, Integer>(s, 1);
          }
        }).reduceByKey(new Function2<Integer, Integer, Integer>() {
			private static final long serialVersionUID = 1L;
		@Override
          public Integer call(Integer x, Integer y) throws Exception {
            // TODO Auto-generated method stub
            return x.intValue() + y.intValue();
          }
        });
    lastCounts.print();
    lastCounts
        .foreach(new Function2<JavaPairRDD<String, Integer>, Time, Void>() {
			private static final long serialVersionUID = 1L;
		public Void call(JavaPairRDD<String, Integer> values, Time time)
              throws Exception {
            values.foreach(new VoidFunction<Tuple2<String, Integer>>() {
				private static final long serialVersionUID = 1L;
			public void call(Tuple2<String, Integer> tuple) throws Exception {
//				wcService.insertWC(new WordCountEntity(tuple._1() + "",Integer.valueOf(tuple._2() + "")));
              }
            });

            return null;
          }
        });

    jssc.start();
    jssc.awaitTermination();
    jssc.stop();
  }
  }
