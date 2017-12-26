package com.seed.transformation;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;

import scala.Tuple2;

public class JoinRDDTest {
	public static void main(String[] args) {
		joinRDD();
	}
	public static void joinRDD(){
		SparkConf sparkConf = new SparkConf().setAppName("joinRDD").setMaster("local");
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);
		List<Tuple2<String,Integer>> list1 = Arrays.asList(
				new Tuple2<String,Integer>("zhangfei",80),
				new Tuple2<String,Integer>("gunayu",72),
				new Tuple2<String,Integer>("zhaoyun",98)
				);
		List<Tuple2<String,Integer>> list2 = Arrays.asList(
				new Tuple2<String,Integer>("zhangfei",1),
				new Tuple2<String,Integer>("gunayu",3),
				new Tuple2<String,Integer>("zhaoyun",2)
				);
				
		JavaPairRDD<String,Integer> scores = ctx.parallelizePairs(list1);
		JavaPairRDD<String,Integer> students = ctx.parallelizePairs(list2);
		//join
		JavaPairRDD<String,Tuple2<Integer,Integer>> res  = scores.join(students);//需要通过key进行join操作
		res.foreach(new VoidFunction<Tuple2<String,Tuple2<Integer,Integer>>>() {//第一个参数类型为两个集合进行关联的key的类型，第二个参数
			//类型为tuple2类型，其两个类型分别是最初两个RDD的value的类型
			private static final long serialVersionUID = 1L;
			public void call(Tuple2<String, Tuple2<Integer, Integer>> tuple)
					throws Exception {
				System.out.println("===========================> <===========================");
				System.out.println("name  ===> " + tuple._1);
				System.out.println("score ===> " + tuple._2._1);
				System.out.println("id ===> " + tuple._2._2);
			}
		});
		
		ctx.close();
	}
}
