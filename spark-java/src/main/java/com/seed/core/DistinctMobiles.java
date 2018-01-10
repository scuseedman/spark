package com.seed.core;

import java.io.File;
import java.io.Serializable;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class DistinctMobiles implements Serializable{
	private static final long serialVersionUID = 1L;
	private void distinctMobiles(String masterName, String inputPath) {
		SparkConf sparkConf = new SparkConf().setAppName("distinct").setMaster(masterName);
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.setLogLevel("WARN");
		//
		JavaRDD<String> lines = jsc.textFile(inputPath);
		System.out.println("total count is ===>>> " + lines.count());
//		JavaPairRDD<String, Integer>  pair_mobiles = lines.mapToPair(new PairFunction<String, String, Integer>() {
//			private static final long serialVersionUID = 1L;
//			public Tuple2<String, Integer> call(String line) throws Exception {
//				return new Tuple2<String,Integer>(line,1);
//			}
//		});
		JavaRDD<String> d_lines = lines.distinct();//对数据进行去重操作
		System.out.println("count of d_lines ===>>> " + d_lines.count());
		d_lines.saveAsTextFile("");
		System.exit(1);
//		JavaPairRDD<String, Integer>  reduce_mobiles = pair_mobiles.reduceByKey(new Function2<Integer, Integer, Integer>() {
//			private static final long serialVersionUID = 1L;
//			public Integer call(Integer v1, Integer v2) throws Exception {
//				return v1 + v2;
//			}
//		});
//		JavaRDD<String> distinct_RDD = reduce_mobiles.map(new Function<Tuple2<String,Integer>, String>() {
//			private static final long serialVersionUID = 1L;
//			public String call(Tuple2<String, Integer> tuple) throws Exception {
//				return tuple._1;
//			}
//		});
//		System.out.println("distinct_RDD's count is ===>>> " + distinct_RDD.count());
//		total count is ===>>> 233120087
//				count of d_lines ===>>> 51743726
		
		jsc.close();
		
	}
	public static void main(String[] args) {
		String masterName = System.getProperty("os.name").toLowerCase().contains("windows")?"local[2]":"yarn-cluster";
		if(args.length != 1)System.exit(1);
		String inputPath = args[0];
//		String outputPath = args[1];
		new DistinctMobiles().distinctMobiles(masterName,inputPath);
	}
	

}
