/** 
 * Project Name:spark-scala 
 * File Name:JavaWordCount.java 
 * Package Name:com.seed.java 
 * Date:2017年12月22日上午9:58:25 
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
/** 
 * ClassName:JavaWordCount
 * Date:     2017年12月22日 上午9:58:25 
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
import scala.Tuple2;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
public class JavaWordCount {
	private static final Pattern SPACE = Pattern.compile(" ");
	public static void main(String[] args) {
		if(args.length < 1){
			System.out.println("===> please input the file : <file> ");
			System.exit(-1);
		}
		/**
		 * 对于所有spark程序来说，要进行所有操作，都需要创建一个sparkconf的上下文对象，在创建上下文的过程中，程序将会向集群申请资源并构建相应的运行环境 
		 * 设置spark程序的应用名称。
		 * 创建的sparkContext 需要的唯一参数就是sparkconf,它是一个<k,v>结构的键值对
		 */
		SparkConf sparkConf = new SparkConf().setAppName("java word count").setMaster("local[1]");
		JavaSparkContext jsc =  new JavaSparkContext(sparkConf);//java context 上下文对象
		jsc.setLogLevel("WARN");//设置程序运行的日志级别
		/**
		 * 利用textFile方法输入数据，返回一个RDD实例对象.RDD的初始创建都是sparkContext负责的，将内存中的集合或者外部文件系统(本地/hdfs等)数据源
		 * RDD:弹性分布式数据集，即一个RDD代表一个被分区的只读数据集，RDD的来源只有两种，一是内存中的集合或者外部文件系统；二是从其他RDD转换操作而来
		 * ，如Map,filter,join等操作 
		 *  textFile读取本地文件需要本地文件在集群的所有节点上，或者通过网络共享该操作
		 */
		JavaRDD<String> lines = jsc.textFile(args[0]);
		/**
		 * flatMap是一个非常常用的函数，简单来说，就是将一条RDD数据使用你自己定义的函数分解为多条RDD数据。
		 * flatMap与map的区别是，flatMap可以输出一个或多个，而map只是生成单一的输出
		 */
		JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {//FlatMapFunction的两个参数分别代表了输入参数的类型与输出参数的类型
			private static final long serialVersionUID = 1L;
			public Iterable<String> call(String line) throws Exception {//call 方法需要自己实现，返回一个Iterable的结构
				return Arrays.asList(SPACE.split(line));//用空格分割单行，生成多个单词
			}
		});
		/**
		 * map 键值对 ，类似于MR的map方法；使用mapToPair方法将一个个单词，生成一个k,v结构的键值对。
		 * new PairFunction的三个参数，第一个为输入参数类型，第二个为输出的k类型，第三个为输出的v的类型
		 * 返回一个Tuple2的新的对象，每个单词为k,value为1
		 */
		JavaPairRDD<String,Integer> ones = words.mapToPair(new PairFunction<String, String, Integer>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, Integer> call(String word) throws Exception {
				return new Tuple2<String,Integer>(word,1);
			}
		});
		/**
		 * 调用reduceByKey方法，按key值进行reduce。
		 * reduceByKey方法，类似于MR的reduce方法。将相同的key进行聚合，而不是相互计算
		 * 要求被操作的数据（ones)是<k,v>键值对的形式
		 * 若ones中包含(word,1),(count,1),(word,1)，将会按word进行聚合，输入为1,1输出为1+1=2即输出为(word,2),(count,1)
		 */
		JavaPairRDD<String,Integer> one = ones.reduceByKey(new Function2<Integer, Integer, Integer>() {
			private static final long serialVersionUID = 1L;
			public Integer call(Integer count1, Integer count2) throws Exception {
				return count1 + count2;
			}
		});
		List<Tuple2<String,Integer>> res = one.collect();
		for(Tuple2<String,Integer> tuple:res){
			System.out.println("单词 " + tuple._1 + " 个数是：===> " + tuple._2);
		}
		jsc.close();
	}
}
