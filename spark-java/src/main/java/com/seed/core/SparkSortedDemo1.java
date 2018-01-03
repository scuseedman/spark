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
import java.util.Comparator;
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
		System.out.println(communities.count());
		JavaPairRDD<String, Lianjia_sh> res = communities.reduceByKey(new Function2<Lianjia_sh, Lianjia_sh, Lianjia_sh>() {
			private static final long serialVersionUID = 1L;
			public Lianjia_sh call(Lianjia_sh entity1, Lianjia_sh entity2)
					throws Exception {
				entity1.setTotalPrice(entity1.getTotalPrice() + entity2.getTotalPrice());
				return entity1;
			}
		});
		JavaRDD<Integer>  result = res.map(new Function<Tuple2<String,Lianjia_sh>, Integer>() {
			private static final long serialVersionUID = 1L;
			public Integer call(Tuple2<String, Lianjia_sh> tuple)
					throws Exception {
				return tuple._2.getTotalPrice();
			}
		});
		List<Integer> finalres = result.take(100);
		for(Integer num : finalres ){
			System.out.println(" ===>>>  " + num);
		}
		jsc.close();
	}
	
	public static void main(String[] args) {
		String masterName = System.getProperty("os.name").toLowerCase().contains("windows") ?"local[1]":"yarn-cluster";//seed
		new SparkSortedDemo1().sortedSparkCount(masterName, args[0]);//master,inputfile
	}
}
