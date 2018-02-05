/** 
 * Project Name:spark-scala 
 * File Name:DataDemo1.java 
 * Package Name:com.seed.java.core 
 * Date:2018年1月31日上午10:50:02 
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
package com.seed.java.core;

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;


/** 
 * ClassName:DataDemo1
 * Date:     2018年1月31日 上午10:50:02 
 * DESC:	可以读取hadoop 压缩的gz压缩文件，不能读取windows下手动压缩的zip文件
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class DataDemo1 {
	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("name").setMaster("local[2]");
		conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
		conf.set("spark.streaming.stopGracefullyOnShutdown","true");
		JavaSparkContext jsc = new JavaSparkContext(conf);
		jsc.setLogLevel("WARN");
		JavaRDD<String> part = jsc.textFile("E:\\deal_data\\part-00017.zip");
		System.out.println(part.count());
		System.out.println(part.first());
		JavaRDD<String> lines = part.flatMap(new FlatMapFunction<String,String>() {
			private static final long serialVersionUID = 1L;
			public Iterable<String> call(String str) throws Exception {
				return Arrays.asList(str.split(","));
			}
			
		});
		System.out.println(lines.first());
		
		jsc.close();
	}
}
