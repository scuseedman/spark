package com.seed.core;

import java.io.Serializable;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class MobileBelong implements Serializable{
	private static final long serialVersionUID = 1L;
	private void groupMobiles(String masterName, String mo_input,
			String pa_input, String output) {
		SparkConf sparkConf = new SparkConf().setAppName("deal").setMaster(masterName);
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.setLogLevel("WARN");
		JavaRDD<String> mo_lines = jsc.textFile(mo_input);
		JavaPairRDD<String, String> pair_mo = mo_lines.mapToPair(new PairFunction<String, String, String>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, String> call(String line) throws Exception {
				return new Tuple2<String,String>(line.split("_")[0],line.split("_")[1]);
			}
		});
		 mo_lines = null;
		JavaRDD<String> pa_lines = jsc.textFile(pa_input);
		JavaPairRDD<String, String>  pair_pa =  pa_lines.mapToPair(new PairFunction<String, String, String>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, String> call(String line) throws Exception {
				return new Tuple2<String,String>(line.split("_")[0],line.split("_")[1]);
			}
		});
		pa_lines = null;
		JavaPairRDD<String, Tuple2<String, String>>  joined = pair_pa.join(pair_mo);
		pair_mo = null;
		pair_pa = null;
		 JavaRDD<String> uniq_joined = joined.map(new Function<Tuple2<String,Tuple2<String,String>>, String>() {
			private static final long serialVersionUID = 1L;
			public String call(Tuple2<String, Tuple2<String, String>> tuple)
					throws Exception {
				return tuple._2._1 + "_" + tuple._2._2;
			}
		});//拼装去重
		 joined = null;
		 JavaRDD<String>  distinct_rdd =  uniq_joined.distinct();
		 uniq_joined = null;
		 JavaPairRDD<String, String>  city_mobiles  = distinct_rdd.mapToPair(new PairFunction<String, String, String>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, String> call(String line) throws Exception {
				return new Tuple2<String,String>(line.split("_")[0],line.split("_")[1]);
			}
		});
		 distinct_rdd = null;
		JavaPairRDD<String, Iterable<String>>  grouped = city_mobiles.groupByKey();
		city_mobiles = null;
		grouped.saveAsTextFile(output);
		jsc.close();
		
	}
	public static void main(String[] args) {
		String masterName = System.getProperty("os.name").toLowerCase().contains("windows")?"local[2]":"yarn-cluster";
		if(args.length != 3)System.exit(1);
		String mo_input = args[0];
		String pa_input = args[1];
		String output = args[2];
		new MobileBelong().groupMobiles(masterName,mo_input,pa_input,output);
		System.out.println(" <<<=== end of the programe !!! ===>>> " + System.currentTimeMillis());
		
	}
}
