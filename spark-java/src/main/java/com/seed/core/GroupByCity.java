package com.seed.core;

import java.util.Iterator;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.Tuple2;

import com.seed.entity.GroupCityEntity;

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
				return line.split("\\|").length == 16;
			}
		});
		System.out.println(">>>+++ " + lines.count());
		JavaRDD<GroupCityEntity>  entityRDD = lines.map(new Function<String, GroupCityEntity>() {
			private static final long serialVersionUID = 1L;
			public GroupCityEntity call(String line) throws Exception {
				return new GroupCityEntity(line);
			}
		}).cache();
		GroupCityEntity  testEntity = entityRDD.first();
		System.out.println(" <<<+++>>> " + testEntity.toString());
		System.out.println(entityRDD.count());
		//tuple2<市，对象>
		JavaPairRDD<String, GroupCityEntity>  pairedRDD = entityRDD.mapToPair(new PairFunction<GroupCityEntity, String, GroupCityEntity>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, GroupCityEntity> call(GroupCityEntity entity)
					throws Exception {
				return new Tuple2<String, GroupCityEntity>(entity.getCity_name(),entity);
			}
		});
		System.out.println("count of  pairedRDD ===>>> " + pairedRDD.count());
		JavaPairRDD<String, Integer>  mobilesInCity = entityRDD.mapToPair(new PairFunction<GroupCityEntity, String, Integer>() {//使用city_name + mobile组合成key进行reduce操作，判断该号码在该地区出现的次数
			private static final long serialVersionUID = 1L;
			public Tuple2<String, Integer> call(GroupCityEntity entity)
					throws Exception {
				return new Tuple2<String,Integer>(entity.getCity_name() + "_" + entity.getMobile(),1);
			}
		});
		//
		JavaPairRDD<String, Integer>  reducedRDD = mobilesInCity.reduceByKey(new Function2<Integer, Integer, Integer>() {
			private static final long serialVersionUID = 1L;
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1 + v2;
			}
		});//如果需要取topN，可以从这里取出哪些号出现该地区次数多
		//再次对数据进行拆分，分成地区和号码
		JavaPairRDD<String, String>  cityedMobile = reducedRDD.mapToPair(new PairFunction<Tuple2<String,Integer>, String, String>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, String> call(Tuple2<String, Integer> tuple)
					throws Exception {
				return new Tuple2<String,String>(tuple._1.split("_")[0],tuple._1.split("_")[1]);
			}
		});
		JavaPairRDD<String, Iterable<String>> mobiles = cityedMobile.groupByKey();
		System.out.println("outputPath >>> " + outputPath);
		mobiles.saveAsTextFile(outputPath );//数据存储为txt文件，按市
		Tuple2<String, Iterable<String>> fi_mobiles = mobiles.first();
		Iterator<String> its = fi_mobiles._2.iterator();
		while(its.hasNext()){
			String mobile = its.next();
			System.out.println(fi_mobiles._1 + " +++>>> " + mobile);
		}
		logger.warn("<<<<<+++++++ 程序执行结束 …………");
		jsc.close();
	}
	public static void main(String[] args) {
		String masterName = System.getProperty("os.name").toLowerCase().contains("windows")?"local[4]":"yarn-cluster";
		if(args.length != 2)System.exit(1);
		String inputPath = args[0];
		
		String outputPath = args[1];
		groupByCity(masterName,inputPath,outputPath);
	}
}
