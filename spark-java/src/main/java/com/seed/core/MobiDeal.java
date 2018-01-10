/** 
 * Project Name:spark-java 
 * File Name:MobiDeal.java 
 * Package Name:com.seed.core 
 * Date:2018年1月10日上午11:18:26 
 * Copyright (c) 2018,  All Rights Reserved. 
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
package com.seed.core;

import java.io.Serializable;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

/** 
 * ClassName:MobiDeal
 * Date:     2018年1月10日 上午11:18:26 
 * DESC:	deal mobile；mobiles => deal mobiles => partners => join => distinct => group => output
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class MobiDeal implements Serializable{
	private static final long serialVersionUID = 1L;
	private void dealMobi(String masterName, String inpath, String outpath,String p_inpath) {
		SparkConf sparkConf = new SparkConf().setAppName("deal").setMaster(masterName);
		sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
		sparkConf.set("spark.streaming.stopGracefullyOnShutdown","true");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.setLogLevel("WARN");
		JavaRDD<String> lines = jsc.textFile(inpath);
		JavaRDD<String> map_mob = lines.map(new Function<String, String>() {
			private static final long serialVersionUID = 1L;
			public String call(String line) throws Exception {
				return line.split("\t")[0] + "_" + line.split("\t")[3];
			}
		});
		JavaRDD<String>  distinct_mob = map_mob.distinct();//去重 mob数据
		map_mob = null;//放空
		distinct_mob.saveAsTextFile(outpath);//数据写盘
		JavaRDD<String>  p_lines = jsc.textFile(p_inpath);//p数据
		JavaPairRDD<String, String>  pair_mob = distinct_mob.mapToPair(new PairFunction<String, String, String>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, String> call(String line) throws Exception {
				return new Tuple2<String,String>(line.split("_")[0].toLowerCase(),line.split("_")[1]);
			}
		});
		
		JavaPairRDD<String, String>  pair_p = p_lines.mapToPair(new PairFunction<String, String, String>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, String> call(String line) throws Exception {
				return new Tuple2<String,String>(line.split("_")[0].toLowerCase(),line.split("_")[1]);
			}
		});
		p_lines =  null;
		JavaPairRDD<String, Tuple2<String, String>>  joined = pair_mob.join(pair_p);//开始关联，取得需要的数据
		pair_mob = null;
		pair_p = null;
		JavaRDD<String> map_res = joined.map(new Function<Tuple2<String,Tuple2<String,String>>, String>() {//对数据去重
			private static final long serialVersionUID = 1L;
			public String call(Tuple2<String, Tuple2<String, String>> tuple)
					throws Exception {
				return tuple._1 + "_" + tuple._2;
			}
		});
		JavaRDD<String> distinct_res = map_res.distinct();//去重操作
		map_res = null;
		JavaPairRDD<String, String>  pair_city_mobi = distinct_res.mapToPair(new PairFunction<String, String, String>() {//
			private static final long serialVersionUID = 1L;
			public Tuple2<String, String> call(String line) throws Exception {
				return  new Tuple2<String,String>(line.split("_")[0],line.split("_")[1]);//市，mobile
			}
		});
		JavaPairRDD<String, Iterable<String>> grouped_cities = pair_city_mobi.groupByKey();
		grouped_cities.saveAsTextFile(outpath);//分好组的数据进行保存
		jsc.close();
	}

	public static void main(String[] args) {
		String masterName = System.getProperty("os.name").toLowerCase().contains("windows")?"local[2]":"yarn-cluster";
		String inpath = args[0];
		String outpath = args[1];
		String p_inpath = args[2];
		new MobiDeal().dealMobi(masterName,inpath,outpath,p_inpath);
		System.out.println(" <<<=== end of the programe !!! ===>>> " + System.currentTimeMillis());
	}

}
