package com.seed.core;

import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class CityMobile {
	private static final Pattern  SPLIT = Pattern.compile(",\\[");
	private static void analyCity(String masterName, String input) {
		SparkConf sparkConf = new SparkConf().setAppName("city").setMaster(masterName);
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.setLogLevel("WARN");
		JavaRDD<String> lines = jsc.textFile(input);
//		System.out.println(lines.first());
		 JavaPairRDD<String, String> maped = lines.mapToPair(new PairFunction<String, String, String>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, String> call(String line) throws Exception {
				return new Tuple2<String,String>(SPLIT.split(line)[0],SPLIT.split(line)[1]);
			}
		});
		 System.out.println(maped.first()._1.replaceAll("\\(", "") + " ===>>> " + maped.first()._2.replaceAll("\\]\\)", ""));
		jsc.close();
	}
	
	public static void main(String[] args) {
		String masterName = System.getProperty("os.name").toLowerCase().contains("windows")?"local[2]":"yarn-cluster";
		if(args.length != 1)System.exit(1);
		String input = args[0];
		analyCity(masterName,input);
	}

}
