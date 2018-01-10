/** 
 * Project Name:spark-java 
 * File Name:PartnerDeal.java 
 * Package Name:com.seed.core 
 * Date:2018年1月10日上午10:35:29 
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
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import com.seed.entity.PartnerEntity;

/** 
 * ClassName:PartnerDeal
 * Date:     2018年1月10日 上午10:35:29 
 * DESC:	重新整理partners数据,得到partn_no_mobile
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class PartnerDeal implements Serializable{
	private static final long serialVersionUID = 1L;
	private void dealPartners(String masterName,String inputPath,String outputPath,String outputPath2) {
		SparkConf sparkConf = new SparkConf().setAppName("deal_p").setMaster(masterName);
		sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
		sparkConf.set("spark.streaming.stopGracefullyOnShutdown","true");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.setLogLevel("WARN");
		JavaRDD<String> lines = jsc.textFile(inputPath);
		System.out.println("count of lines ===>>> " + lines.count());
		lines = lines.filter(new Function<String, Boolean>() {
			private static final long serialVersionUID = 1L;
			public Boolean call(String line) throws Exception {
				return line.split("\\|").length == 15;
			}
		});
		JavaRDD<String>  map_entity = lines.map(new Function<String, String>() {
			private static final long serialVersionUID = 1L;
			public String call(String line) throws Exception {
				return new PartnerEntity(line).toString();
			}
		});
		map_entity.saveAsTextFile(outputPath);//detail version
		JavaRDD<String> p_city = map_entity.map(new Function<String, String>() {
			private static final long serialVersionUID = 1L;
			public String call(String line) throws Exception {
				return line.split("\\|")[0] + "_" + line.split("\\|")[14];
			}
		});
		p_city.saveAsTextFile(outputPath2);
		jsc.close();
	}
	public static void main(String[] args) {
		String masterName = System.getProperty("os.name").toLowerCase().contains("windows")?"local[2]":"yarn-cluster";
		if(args.length != 3)System.exit(1);
		String inputPath = args[0];
		String outputPath = args[1];
		String outputPath2 = args[2];
		new PartnerDeal().dealPartners(masterName,inputPath,outputPath,outputPath2);
		
	}
}
