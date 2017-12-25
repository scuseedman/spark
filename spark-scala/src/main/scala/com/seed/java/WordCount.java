/** 
 * Project Name:spark-scala 
 * File Name:WordCount.java 
 * Package Name:com.seed.java 
 * Date:2017年12月25日上午10:10:17 
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
package com.seed.java;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

/** 
 * ClassName:WordCount
 * Date:     2017年12月25日 上午10:10:17 
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class WordCount {
	public static void main(String[] args) {
		final Pattern SPACE = Pattern.compile(",");
		SparkConf sparkConf = new SparkConf();
		sparkConf.setAppName("java hello world  ").setMaster("local[1]");
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);
		ctx.setLogLevel("WARN");
		JavaRDD<String> jrdd = ctx.textFile(args[0]);//文件输入，适当时候需要加上泛型，否则类型告警
//		JavaRDD<String> words = jrdd.flatMap(new FlatMapFunction<String,String>() {//切分成一个个单词的数组
//			private static final long serialVersionUID = 1L;
//			public Iterable<String> call(String line) throws Exception {
//				return Arrays.asList(SPACE.split(line));
//			}
//		});
		JavaRDD<String> words = jrdd.flatMap(new FlatMapFunction<String, String>() {//FlatMapFunction的两个参数分别代表了输入参数的类型与输出参数的类型
			private static final long serialVersionUID = 1L;
			public Iterable<String> call(String line) throws Exception {//call 方法需要自己实现，返回一个Iterable的结构
				return Arrays.asList(SPACE.split(line));//用空格分割单行，生成多个单词  
				//为什么会提示告警呢？？？原来是引入了不同的Arrays类
			}
		});
		JavaPairRDD<String,Integer> jprdd = words.mapToPair(new PairFunction<String, String, Integer>() {//将所有单词分别计数为1
			private static final long serialVersionUID = 1L;
			public Tuple2<String, Integer> call(String word) throws Exception {
				return new Tuple2<String,Integer>(word,1);
			}
		});
		JavaPairRDD<String,Integer> res = jprdd.reduceByKey(new Function2<Integer, Integer, Integer>() {//对相同的key执行reduce操作
			private static final long serialVersionUID = 1L;
			public Integer call(Integer count1, Integer count2) throws Exception {
				return count1 + count2;
			}
		});
		List<Tuple2<String,Integer>> results = res.collect();
		for(Tuple2<String,Integer> tuple:results){
			System.out.println(tuple._1 + " ===> " + tuple._2);
		}
		ctx.close();
	}
}
