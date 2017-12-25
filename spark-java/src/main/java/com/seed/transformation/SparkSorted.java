package com.seed.transformation;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;

import scala.Tuple2;

public class SparkSorted {
	public static void main(String[] args) {
		SparkConf sparkConf = new SparkConf().setAppName("sort by key ").setMaster(args[0]);
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);
		ctx.setLogLevel("WARN");
		List<Tuple2<Integer,String>> scoreList = Arrays.asList(
				new Tuple2<Integer,String>(98,"zhangfei"),
				new Tuple2<Integer,String>(72,"liubei"),
				new Tuple2<Integer,String>(113,"guanyu"),
				new Tuple2<Integer,String>(83,"zhaoyun")
				);
		JavaPairRDD<Integer,String> scores = ctx.parallelizePairs(scoreList);//并行化
		JavaPairRDD<Integer,String> sorted = scores.sortByKey();//如果设置为false，则为降序，默认为升序 
		sorted.foreach(new VoidFunction<Tuple2<Integer,String>>() {
			private static final long serialVersionUID = 1L;
			public void call(Tuple2<Integer, String> tuple) throws Exception {
				System.out.println(tuple._1 + " ===> " + tuple._2);
				//输出结果不对？？_seed; 原因，如果设置master为local[2]时，将会在单独主机上进行
				//排序而不是整体排序，只是对单独主机有序。与hive中的sort by 是类似的，局部排序而非全局排序！！
			}
		});
		ctx.close();
		
		
		
	}
}
