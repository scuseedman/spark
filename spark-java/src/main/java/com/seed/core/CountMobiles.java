package com.seed.core;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class CountMobiles implements Serializable{
	private static final long serialVersionUID = 1L;
	public void countMobiles(String masterName,String inputPath,String outputPath){
		SparkConf sparkConf = new SparkConf().setAppName("count").setMaster(masterName);
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.setLogLevel("WARN");
		JavaRDD<String> lines = jsc.textFile(inputPath);
		System.out.println("count of lines ===>>> " + lines.count());
		lines = lines.filter(new Function<String, Boolean>() {
			private static final long serialVersionUID = 1L;
			public Boolean call(String line) throws Exception {
				return line.split("\t").length == 4;
			}
		});
		System.out.println("count of lines ===>>> " + lines.count());
		JavaRDD<String> mobiles = lines.map(new Function<String, String>() {
			private static final long serialVersionUID = 1L;
			public String call(String line) throws Exception {
				return line.split("\t")[3];
			}
		});
		JavaPairRDD<String,Integer> pair_mobiles = mobiles.mapToPair(new PairFunction<String, String, Integer>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, Integer> call(String mobile) throws Exception {
				return new Tuple2<String ,Integer>(mobile,1);
			}
		});
		JavaPairRDD<String, Integer> reduce_mobiles = pair_mobiles.reduceByKey(new Function2<Integer, Integer, Integer>() {
			private static final long serialVersionUID = 1L;
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1 + v2;
			}
		});
		JavaRDD<String> distinct_mobiles = reduce_mobiles.map(new Function<Tuple2<String,Integer>, String>() {
			private static final long serialVersionUID = 1L;
			public String call(Tuple2<String, Integer> tuple) throws Exception {
				return tuple._1;
			}
		});
		System.out.println("distinct mobiles ===>>> " + distinct_mobiles.count());
		distinct_mobiles.saveAsTextFile(outputPath);
		jsc.close();
//		return distinct_mobiles;
	}
	public static void main(String[] args) {
		String masterName = System.getProperty("os.name").toLowerCase().contains("windows")?"local[2]":"yarn-cluster";
		if(args.length != 2)System.exit(1);
		String inputPath = args[0];
		String outputPath = args[1];
		if(new File(inputPath).isFile())System.exit(1);
		File[] files = new File(inputPath).listFiles();
//		List<JavaRDD<String>> list = new ArrayList<JavaRDD<String>>();
		for(File file : files){
			System.out.println("full path >>>=== " + file.getAbsolutePath());
			System.out.println(file.getName());
			String output  = new File(outputPath).getAbsolutePath() + File.separator + file.getName().replaceAll(".gz", "");
			System.out.println(output);
			new CountMobiles().countMobiles(masterName,file.getAbsolutePath(),output);
//			list.add(res);
		}
		
	}
}
