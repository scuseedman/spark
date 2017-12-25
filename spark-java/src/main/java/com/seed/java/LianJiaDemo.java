/** 
 * Project Name:spark-java 
 * File Name:LianJiaDemo.java 
 * Package Name:com.seed.java 
 * Date:2017年12月25日上午11:20:32 
 * Copyright (c) 2017,  All Rights Reserved. 
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
package com.seed.java;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

import com.seed.entity.LianJiaEntity;


/** 
 * ClassName:LianJiaDemo
 * Date:     2017年12月25日 上午11:20:32 
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class LianJiaDemo {
	public static final Pattern SPLIT = Pattern.compile(",");
	private static Logger logger = Logger.getLogger(LianJiaDemo.class);
	public static void main(String[] args) {
		System.out.println("the programme should input 2 arguments for it . one  input file ,other set master !!!");
		if(args.length < 2 ){
			logger.warn("where is the input <file>  or the master ???, please check it !!");
			System.exit(1);
		}
		SparkConf sparkConf = new SparkConf().setAppName("lj-demo").setMaster(args[1])
				.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);//context
		JavaRDD<String> lines = ctx.textFile(args[0]);//input file
		lines = lines.filter(new Function<String, Boolean>() {
			private static final long serialVersionUID = 1L;
			public Boolean call(String line) throws Exception {
				if(SPLIT.split(line).length == 8)return true;//切分得到正确栏数则使用，否则数据丢弃
				return false;
			}
		});
		JavaRDD<LianJiaEntity> entities = lines.flatMap(new FlatMapFunction<String, LianJiaEntity>() {
			private static final long serialVersionUID = 1L;
			public Iterable<LianJiaEntity> call(String line) throws Exception {
				return Arrays.asList(new LianJiaEntity(line));
			}
		});
		JavaPairRDD<String, LianJiaEntity> isLifts = entities.mapToPair(new PairFunction<LianJiaEntity, String, LianJiaEntity>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, LianJiaEntity> call(LianJiaEntity entity)
					throws Exception {
				return new Tuple2<String,LianJiaEntity>(entity.getIsLift(),entity);//
			}
		});
		JavaPairRDD<String,LianJiaEntity> result = isLifts.reduceByKey(new Function2<LianJiaEntity, LianJiaEntity, LianJiaEntity>() {
			private static final long serialVersionUID = 1L;
			public LianJiaEntity call(LianJiaEntity entity1, LianJiaEntity entity2)
					throws Exception {
//				entity1.setTotalPrice(Integer.valueOf(entity1.getTotalPrice()) + Integer.valueOf(entity2.getTotalPrice()) + "");
				entity1.setHouseName(entity1.getHouseName() + "|" + entity2.getHouseName());
				return entity1;
			}
		});
		List<Tuple2<String,LianJiaEntity>> res = result.collect();
		for(Tuple2<String,LianJiaEntity> tuple:res){
			System.out.println(tuple._1 + " ===> " + tuple._2.getHouseName());
		}
		ctx.close();
	}
}
