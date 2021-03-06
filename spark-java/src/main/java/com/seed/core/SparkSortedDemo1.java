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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
public class SparkSortedDemo1 implements Serializable{
	private static final long serialVersionUID = -3409336457696700447L;
	public static final Pattern SPLIT = Pattern.compile(",");
	
	public void sortedSparkCount(String masterName,String path ){
		SparkConf sparkConf = new SparkConf().setMaster(masterName).setAppName("sd_demo1");
		//需要指定序列化的类
		sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.setLogLevel("WARN");
		JavaRDD<String> lines = jsc.textFile(path);
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
		JavaPairRDD<String, Lianjia_sh>  communities = entities.mapToPair(new PairFunction<Lianjia_sh, String, Lianjia_sh>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, Lianjia_sh> call(Lianjia_sh entity)
					throws Exception {
				return new Tuple2<String, Lianjia_sh>(entity.getCommunityName(),entity);
			}
		});
		System.out.println("reducebykey前总数 ===>>> " + communities.count());
		JavaPairRDD<String, Lianjia_sh> res = communities.reduceByKey(new Function2<Lianjia_sh, Lianjia_sh, Lianjia_sh>() {
			private static final long serialVersionUID = 1L;
			public Lianjia_sh call(Lianjia_sh entity1, Lianjia_sh entity2)
					throws Exception {
				entity1.setTotalPrice(entity1.getTotalPrice() + entity2.getTotalPrice());
				return entity1;
			}
		});
		System.out.println("reducebykey后总数 ===>>> " + res.count());
		JavaPairRDD<String, Iterable<Lianjia_sh>>  result = communities.groupByKey().cache();//group by key; group后取前三
		result.foreach(new VoidFunction<Tuple2<String,Iterable<Lianjia_sh>>>() {
			private static final long serialVersionUID = 1L;
			public void call(Tuple2<String, Iterable<Lianjia_sh>> tuple)
					throws Exception {
				System.out.println("group ===>>> " + tuple._1);
				Iterator<Lianjia_sh> its = tuple._2.iterator();//java迭代器
				while(its.hasNext()){
					Lianjia_sh sh = its.next();
					System.out.println(" value ===>> " + sh.getRoomNum() + " ===> " + sh.getDesc() + " ===> " + sh.getTotalPrice());
				} 
			}
		});
		System.out.println(" ========================================== 分组取前三 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		JavaPairRDD<String, Iterable<Lianjia_sh>>  topN = result.mapToPair(new PairFunction<Tuple2<String,Iterable<Lianjia_sh>>, String, Iterable<Lianjia_sh>>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, Iterable<Lianjia_sh>> call(
					Tuple2<String, Iterable<Lianjia_sh>> tuple) throws Exception {
				// TODO Auto-generated method stub
				List<Lianjia_sh> list = new ArrayList<Lianjia_sh>();//将该组中的所有元素装入集合中,最后结果都是以集合返回
				Iterator<Lianjia_sh> its = tuple._2.iterator();//需要判断每组是否够3个
				while(its.hasNext()){
					//需要取出，否则呵呵哒 。。。
					list.add(its.next());//所有数据加入数组中，然后对数据进行排序取前三？_seed
				}
				Collections.sort(list);//进行排序
				if(list.size() <= 3 )return new Tuple2<String, Iterable<Lianjia_sh>>(tuple._1,list);
				//如果集合大于3，取出前三个元素
				List<Lianjia_sh> list1 = new ArrayList<Lianjia_sh>(3);
				list1.add(list.get(0));
				list1.add(list.get(1));
				list1.add(list.get(2));
				return new Tuple2<String, Iterable<Lianjia_sh>>(tuple._1,list1);//返回前三个
			}
		});
		System.out.println("topN ===>> " + topN.count());
		topN.foreach(new VoidFunction<Tuple2<String,Iterable<Lianjia_sh>>>() {
			private static final long serialVersionUID = 1L;
			public void call(Tuple2<String, Iterable<Lianjia_sh>> tuple)
					throws Exception {
				// TODO Auto-generated method stub
				System.out.println("group ===>>> " + tuple._1);
				Iterator<Lianjia_sh> g_topN = tuple._2.iterator();
				while(g_topN.hasNext()){
					Lianjia_sh lsh = g_topN.next();
					System.out.println(lsh.getCommunityName() + " ===>>> " + lsh.getTotalPrice() + " ===<>>" + lsh.getDesc());
				}
				
			}
		});
		System.out.println(" ====================== reduce后 >>>>>>>>>>>>>>>>>>>>>>>>>>>");
		res.foreach(new VoidFunction<Tuple2<String,Lianjia_sh>>() {
			private static final long serialVersionUID = 1L;
			public void call(Tuple2<String, Lianjia_sh> tuple) throws Exception {
//				System.out.println(tuple._1 + " ===>>> " + tuple._2.getTotalPrice() + " ===>>> " + tuple._2.getDesc());
			} 
		});
		jsc.close();
	}
	// 需要借两本书；1、redis入门 ；2、spark大数据编程
	public static void main(String[] args) {
		String masterName = System.getProperty("os.name").toLowerCase().contains("windows") ?"local[1]":"yarn-cluster";//seed
		new SparkSortedDemo1().sortedSparkCount(masterName, args[0]);//master,inputfile
	}
}
