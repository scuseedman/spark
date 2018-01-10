/** 
 * Project Name:spark-java 
 * File Name:RDDJoinInCore.java 
 * Package Name:com.seed.core 
 * Date:2018年1月8日下午3:24:57 
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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.netty.util.internal.StringUtil;

import org.apache.hadoop.hive.ql.parse.HiveParser_IdentifiersParser.sysFuncNames_return;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;

import com.google.common.base.Optional;
import com.seed.entity.MobileEntity;
import com.seed.entity.PartnerEntity;
import com.seed.utils.IOUtil;

import scala.Tuple2;

/** 
 * ClassName:RDDJoinInCore
 * Date:     2018年1月8日 下午3:24:57 
 * DESC:	测试两个RDD的join 代码测试可产生合理的结果 _seed
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class RDDJoinInCore {
	public static void rddJoin(String masterName,String partnerPath,String mobilePath){
		SparkConf sparkConf = new SparkConf().setAppName("join").setMaster(masterName);
		sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
		sparkConf.set("spark.streaming.stopGracefullyOnShutdown","true");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		jsc.setLogLevel("WARN");
		JavaRDD<String>  partners = jsc.textFile(partnerPath,1);//可以加上指定的partitions 数
		System.out.println(partners.count());
		JavaRDD<String>  f_partners = partners.filter(new Function<String, Boolean>() {
			private static final long serialVersionUID = 1L;
			public Boolean call(String line) throws Exception {
				return line.split("\\|").length == 15;
//				return new StringBuilder(line).reverse().toString().split("\\|").length == 15;
			}
		});
		System.out.println(f_partners.count());
		JavaPairRDD<String, String>  pair_partners = f_partners.mapToPair(new PairFunction<String, String, String>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, String> call(String line) throws Exception {
				return new Tuple2<String,String>(line.split("\\|")[0].toLowerCase(),line);//tuple2(partner_no,line)
			}
		});
		System.out.println("pari_partners ===>>> " + pair_partners.count());
		
		
		JavaRDD<String>  mobiles = jsc.textFile(mobilePath,1);
		System.out.println(" ===>>> " + mobiles.count());
		JavaRDD<String>  f_mobiles = mobiles.filter(new Function<String, Boolean>() {
			private static final long serialVersionUID = 1L;
			public Boolean call(String mobile) throws Exception {
				return mobile.split("\t").length == 4;
			}
		});
		System.out.println("f ===>>> " + f_mobiles.count());
		JavaPairRDD<String, String>  pair_mobiles = f_mobiles.mapToPair(new PairFunction<String, String, String>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, String> call(String mobile) throws Exception {
				return new Tuple2<String,String>(mobile.split("\t")[0].toLowerCase(),mobile);//tuple2(partner_no,mobile)
			}
		});
		System.out.println("pair_mobiles ===>>> " + pair_mobiles.count());
		JavaPairRDD<String, PartnerEntity>  pair_entity_partners = pair_partners.mapToPair(new PairFunction<Tuple2<String,String>, String, PartnerEntity>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, PartnerEntity> call(
					Tuple2<String, String> tuple) throws Exception {
				return new Tuple2<String,PartnerEntity>(tuple._1,new PartnerEntity(tuple._2));
			}
		});
		JavaPairRDD<String, MobileEntity>  pair_entity_mobiles = pair_mobiles.mapToPair(new PairFunction<Tuple2<String,String>, String, MobileEntity>() {
			private static final long serialVersionUID = 1L;
			public Tuple2<String, MobileEntity> call(Tuple2<String, String> tuple)
					throws Exception {
				return new Tuple2<String, MobileEntity>(tuple._1,new MobileEntity(tuple._2));
			}
		});
		
		JavaPairRDD<String, Tuple2<PartnerEntity, MobileEntity>>  joined_res = pair_entity_partners.join(pair_entity_mobiles);
//		joined_res.foreachPartition(new VoidFunction<Iterator<Tuple2<String,Tuple2<PartnerEntity,MobileEntity>>>>() {
//			private static final long serialVersionUID = 1L;
//			public void call(
//					Iterator<Tuple2<String, Tuple2<PartnerEntity, MobileEntity>>> its)
//					throws Exception {
////				List<Tuple2<PartnerEntity, MobileEntity>> list = new ArrayList<Tuple2<PartnerEntity, MobileEntity>>();
//				List<String> list = new ArrayList<String>();
//				while(its.hasNext()){
//					Tuple2<String, Tuple2<PartnerEntity, MobileEntity>> tuple = its.next();
//					list.add(tuple._2._2.getMobile() + "|" + tuple._2._1.getPartner_no() + "|" + tuple._2._1.getSc_pname() +
//						"|" + tuple._2._1.getSc_name() + "|" + tuple._2._1.getSc_pid() + "|" + tuple._2._1.getSc_id());//将这个集合写入文件中
//					
////					System.out.println(tuple._2._2.getMobile() + " >>>=== " + tuple._2._1.toString());
//				}
//				IOUtil.saveCollection(list, new File("F:\\deal_data_msh\\final_file_000003_0.txt"));
//				
//			}
//		});
		joined_res.saveAsTextFile("");
		//inner join 内关联
//		JavaPairRDD<String, Tuple2<String, String>>  join_rdd = pair_partners.join(pair_mobiles);//基于key的两个行数据关联在一起了 _seed
//		join_rdd.foreach(new VoidFunction<Tuple2<String,Tuple2<String,String>>>() {
//			private static final long serialVersionUID = 1L;
//			public void call(Tuple2<String, Tuple2<String, String>> res)
//					throws Exception {
//				System.out.println(res._1 + " ===>>> " + res._2._1 + " <<<=== " + res._2._2);
//			}
//		});
//		JavaPairRDD<String, Tuple2<String, Optional<String>>>  leftoutjoin_res = pair_partners.leftOuterJoin(pair_mobiles);//基于key的关联
//		leftoutjoin_res.foreachPartition(new VoidFunction<Iterator<Tuple2<String,Tuple2<String,Optional<String>>>>>() {
//			private static final long serialVersionUID = 1L;
//			public void call(
//					Iterator<Tuple2<String, Tuple2<String, Optional<String>>>> its)
//					throws Exception {
//				while(its.hasNext()){
//					Tuple2<String, Tuple2<String, Optional<String>>> tuple = its.next();
//					if(tuple._2._2.asSet().size() > 0)//相当于是inner join的效果 _seed
//						System.out.println(tuple._1 + " ===>>> " + tuple._2._1 + " <<<=== " + tuple._2._2.asSet());
//				}
//			}
//		});
		jsc.close();
	}
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		String masterName = System.getProperty("os.name").toLowerCase().contains("windows")?"local[4]":"yarn-cluster";
		if(args.length != 3){
			System.exit(1);
		}
		String partnerPath = args[0];
		String mobilePath = args[1];
		String outPath = args[2];
		rddJoin( masterName,partnerPath,mobilePath);
		System.out.println(System.currentTimeMillis());
	}
}
