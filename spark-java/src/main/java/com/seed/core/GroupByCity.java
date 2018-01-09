package com.seed.core;

import java.util.Iterator;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupByCity {
	private static  Logger logger = LoggerFactory.getLogger(GroupByCity.class);
	public static void groupByCity(String masterName,String inputPath,String outputPath){
		SparkConf sparkConf = new SparkConf().setAppName("group").setMaster(masterName);
		sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
		sparkConf.set("spark.streaming.stopGracefullyOnShutdown","true");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.setLogLevel("WARN");
		JavaRDD<String> lines = jsc.textFile(inputPath);
		System.out.println(lines.count());
		lines = lines.filter(new Function<String, Boolean>() {
			private static final long serialVersionUID = 1L;
			public Boolean call(String line) throws Exception {
				return line.split("\\|").length != 6;
			}
		});
		System.out.println(">>>+++ " + lines.count());
		lines.foreachPartition(new VoidFunction<Iterator<String>>() {
			private static final long serialVersionUID = 1L;
			public void call(Iterator<String> its) throws Exception {
				int nums = 0;
				while(its.hasNext()){
					String str = its.next();
					System.out.println(">>>++++ " + str);
					nums += 1;
					if(nums >= 10)break;//limit效果
				}
			}
		});
		logger.warn("<<<<<+++++++ 程序执行结束 …………");
		jsc.close();
	}
	public static void main(String[] args) {
		String masterName = System.getProperty("os.name").toLowerCase().contains("windows")?"local[4]":"yarn-cluster";
		if(args.length != 2)System.exit(1);
		String inputPath = args[0];//	F:\\deal_data_msh\\mid_res_mobile_city
		
		String outputPath = args[1];
		groupByCity(masterName,inputPath,outputPath);
	}
}
