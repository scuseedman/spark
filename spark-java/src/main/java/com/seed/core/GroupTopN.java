/** 
 * Project Name:spark-java 
 * File Name:GroupTopN.java 
 * Package Name:com.seed.core 
 * Date:2018年3月14日下午3:34:18 
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
 * 　　　　　　　　　┃　　　┃　欣赏一个人,始于颜值,敬于智慧,久于善良,终于人品.　　　　　　　　　　
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
 * 　　　　┃　　　┃    欣赏一个人,始于颜值,敬于智慧,久于善良,终于人品.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;

import scala.Tuple2;

import com.seed.entity.StockEntity;


/** 
 * ClassName:GroupTopN
 * Date:     2018年3月14日 下午3:34:18 
 * DESC:	
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class GroupTopN {
	public static void main(String[] args){
		SparkConf conf = new SparkConf();
		conf.setAppName("group top N ").setMaster("local[2]");
		JavaSparkContext sc = new JavaSparkContext(conf);
		sc.setLogLevel("WARN");
		JavaRDD<String> lines = sc.textFile("sz000006_2018-03-13").filter(new Function<String, Boolean>() {
			private static final long serialVersionUID = 1L;
			public Boolean call(String line) throws Exception {
				return (line.startsWith("0") || line.startsWith("1"))&& line.split("\t").length == 6;
			}
		});
		JavaPairRDD<Double,StockEntity> pairs_rdd = lines.mapToPair(new PairFunction<String, Double,StockEntity>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<Double, StockEntity> call(String line)
					throws Exception {
				return new Tuple2<Double,StockEntity>(Double.valueOf(line.split("\t")[1]),new StockEntity(line));
			}
		});
		JavaPairRDD<Double, Iterable<StockEntity>> groupByKey_rdds = pairs_rdd.groupByKey();
		JavaPairRDD<Double, Iterable<StockEntity>> group_topN = groupByKey_rdds.mapToPair(new PairFunction<Tuple2<Double, Iterable<StockEntity>>, Double, Iterable<StockEntity>>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<Double, Iterable<StockEntity>> call(
					Tuple2<Double, Iterable<StockEntity>> tuple)
					throws Exception {
				Iterator<StockEntity> its = tuple._2.iterator();
				List<StockEntity> res = new ArrayList<StockEntity>();
				while(its.hasNext()){
					res.add(its.next());
				}
				Collections.sort(res);
				if(res.size() >= 3){
					List<StockEntity> subList = res.subList(0, 3);
					return new Tuple2<Double, Iterable<StockEntity>>(tuple._1,subList);
				}else{
					return new Tuple2<Double, Iterable<StockEntity>>(tuple._1,res);
				}
//				繁文缛节
				
			}
		});
		group_topN.foreach(new VoidFunction<Tuple2<Double,Iterable<StockEntity>>>() {
			private static final long serialVersionUID = 1L;
			public void call(Tuple2<Double, Iterable<StockEntity>> tuple)
					throws Exception {
				System.out.println(tuple._1 + " ===>>> " + tuple._2);
			}
		});
		
		
		
		sc.close();
		
		
	}
}
