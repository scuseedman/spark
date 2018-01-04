/** 
 * Project Name:spark-java 
 * File Name:SparkSortedDemo1.java 
 * Package Name:com.seed.core 
 * Date:2018年1月3日上午10:31:53 
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;

import scala.Tuple2;

import com.seed.entity.Lianjia_sh;

/** 
 * ClassName:SparkSortedDemo1
 * Date:     2018年1月3日 上午10:31:53 
 * DESC:	对spark的计算结果按value进行排序
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class SparkSortedDemo2 implements Serializable{
	private static final long serialVersionUID = -3409336457696700447L;
	public static final Pattern SPLIT = Pattern.compile(",");
	
	public void sortedSparkCount(String masterName,String path ){
		SparkConf sparkConf = new SparkConf().setMaster(masterName).setAppName("sd_demo1");
		sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.setLogLevel("WARN");
		JavaRDD<String> lines = jsc.textFile(path).cache();
		lines = lines.filter(new Function<String, Boolean>() {
			private static final long serialVersionUID = 1L;
			public Boolean call(String line) throws Exception {
				if(SPLIT.split(line).length == 8)return true;//切分得到正确栏数则使用，否则数据丢弃
				return false;
			}
		});
		System.out.println(lines.count());
		JavaRDD<Lianjia_sh> entities = lines.flatMap(new FlatMapFunction<String, Lianjia_sh>() {
			private static final long serialVersionUID = 1L;
			public Iterable<Lianjia_sh> call(String line)
					throws Exception {
				return Arrays.asList(new Lianjia_sh(line));
			}
		});
		System.out.println(entities.count());
		JavaPairRDD<Lianjia_sh, Lianjia_sh>  communities = entities.mapToPair(new PairFunction<Lianjia_sh, Lianjia_sh, Lianjia_sh>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<Lianjia_sh,Lianjia_sh> call(Lianjia_sh entity)
					throws Exception {
				return new Tuple2<Lianjia_sh, Lianjia_sh>(entity,entity);
			}
		});
		System.out.println(communities.count());
		JavaPairRDD<Lianjia_sh, Lianjia_sh> res = communities.reduceByKey(new Function2<Lianjia_sh, Lianjia_sh, Lianjia_sh>() {
			private static final long serialVersionUID = 1L;
			public Lianjia_sh call(Lianjia_sh entity1, Lianjia_sh entity2)
					throws Exception {
				entity1.setTotalPrice(entity1.getTotalPrice() + entity2.getTotalPrice());
				return entity1;
			}
		});
		JavaPairRDD<Lianjia_sh, Lianjia_sh>  result = res.sortByKey();
		JavaRDD<Lianjia_sh> f_res = result.map(new Function<Tuple2<Lianjia_sh,Lianjia_sh>, Lianjia_sh>() {
			private static final long serialVersionUID = 1L;
			public Lianjia_sh call(Tuple2<Lianjia_sh, Lianjia_sh> tuple)
					throws Exception {
				return tuple._2;
			}
		});
		f_res.foreach(new VoidFunction<Lianjia_sh>() {
			private static final long serialVersionUID = 1L;
			public void call(Lianjia_sh entity) throws Exception {
				System.out.println(entity.getCommunityName() + " ===>>> " + entity.getTotalPrice() + " ===>>> " + entity.getUnitPrice());
			} 
		});
		System.out.println(" 小区总数 ===>>> " + f_res.count());
		System.out.println("==========>>>>>>>>>>>>>>> 开始找出大于2个房源数的小区 ");
		JavaRDD<String> communites = lines.flatMap(new FlatMapFunction<String, String>() {//解析得到小区名
			private static final long serialVersionUID = 1L;
			public Iterable<String> call(String house) throws Exception {
				return Arrays.asList(house.split(",")[0]);
			}
		});
		JavaPairRDD<String, Integer>  p_houses = communites.mapToPair(new PairFunction<String, String, Integer>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, Integer> call(String houseName) throws Exception {
				return new Tuple2<String,Integer>(houseName,1);
			}
		});
		JavaPairRDD<String, Integer> f_houses = p_houses.reduceByKey(new Function2<Integer, Integer, Integer>() {
			private static final long serialVersionUID = 1L;
			public Integer call(Integer count1, Integer count2) throws Exception {
				return count1 + count2 ;
			}
		});
		List<Tuple2<String, Integer>> t_houses = f_houses.collect();
		for(Tuple2<String,Integer> tuple: t_houses){
			if(tuple._2 >=2){
				System.out.println(tuple._1 + " ===>>> " + tuple._2);
			}
		}
		System.out.println("t_houses.size ===>>> " + t_houses.size());
		jsc.close();
	}
	
	// 需要借两本书；1、redis入门 ；2、spark大数据编程
	public static void main(String[] args) {
		String masterName = System.getProperty("os.name").toLowerCase().contains("windows") ?"local[1]":"yarn-cluster";//seed
		new SparkSortedDemo2().sortedSparkCount(masterName, args[0]);//master,inputfile
	}
}
