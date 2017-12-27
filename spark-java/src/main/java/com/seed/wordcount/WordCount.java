/** 
 * Project Name:spark-java 
 * File Name:SimpleApp.java 
 * Date:2017年12月7日上午11:09:16 
 * Copyright (c) 2017,  All Rights Reserved. 
 * 
*/ 
package com.seed.wordcount;
/** 
 * ClassName:SimpleApp
 * Date:     2017年12月7日 上午11:09:16 
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;


public class WordCount {
	public static void main(String[] args) {
        String logFile = args[0];
        SparkConf conf =new SparkConf().setAppName("spark wc in java");
        conf.setMaster("local");
        
        JavaSparkContext sc = new JavaSparkContext(conf);
        sc.setLogLevel("WARN");
        JavaRDD<String> logData = sc.textFile(logFile).cache();
        System.out.println("the total line is : " + logData.count());
        long aStartWith = logData.filter(new Function<String, Boolean>() {//点击进入eclipse被异常退出 seed
			private static final long serialVersionUID = 1L;
			public Boolean call(String s) throws Exception {
				return s.split(" ")[0].startsWith("a");
			}
		}).count();
        System.out.println("start with a ,the counts is : " + aStartWith);
        
        JavaRDD<String>  words = logData.flatMap(new FlatMapFunction<String, String>() {
			private static final long serialVersionUID = 1L;
			public Iterable<String> call(String s) throws Exception {
				return Arrays.asList(s.split(" "));
			}
		});
        System.out.println("............将要输出第一个JavaRDD ,分割的词组");
        System.out.println(words.collect().toString());
        
        // (word,1)
        JavaPairRDD<String, Integer> counts = words.mapToPair(new PairFunction<String, String, Integer>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, Integer> call(String s) throws Exception {
				return new Tuple2<String,Integer>(s,1);
			}
		});
        System.out.println("输出第二个JavaRDD ,每个词计数为1");
        System.out.println(counts);
        
        JavaPairRDD<String, Integer> totals = counts.reduceByKey(new Function2<Integer, Integer, Integer>() {
			private static final long serialVersionUID = 1L;
			public Integer call(Integer a1, Integer a2) throws Exception {
				return a1 + a2 ;
			}
		});
        System.out.println("输出第三个JavaRDD ,每个词计数为1");
//		List<Tuple2<String, Integer>> output = totals.collect();
        for(Tuple2<String,Integer> tuple :totals.collect()){
        	System.out.println(tuple._1() + " : " + tuple._2());
        } 
        sc.stop();
        sc.close();
	}
}
