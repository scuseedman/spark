/** 
 * Project Name:spark-java 
 * File Name:SecondarySortDemo.java 
 * Package Name:com.seed.core 
 * Date:2018年3月14日上午10:58:28 
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

import java.io.Serializable;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;

import scala.Tuple2;
import scala.math.Ordered;

/** 
 * ClassName:SecondarySortDemo
 * Date:     2018年3月14日 上午10:58:28 
 * DESC:	基于java的spark 二次排序
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class SecondarySortKey implements Ordered<SecondarySortKey> ,Serializable{
	private static final long serialVersionUID = 1L;
	private Integer tradeMoneys;//交易额
	private Integer tradeHands;//手数
	//排序先按交易额再按交易手数
	
	public static void main(String[] args) {
		SparkConf conf = new SparkConf();
		conf.setAppName("secondary sorted").setMaster("local[2]");
		JavaSparkContext jsc = new JavaSparkContext(conf); 
		jsc.setLogLevel("WARN");
		JavaRDD<String> lines = jsc.textFile("sz000006_2018-03-13",1).filter(new Function<String, Boolean>() {
			private static final long serialVersionUID = 1L;
			public Boolean call(String line) throws Exception {
				return line.startsWith("0") || line.startsWith("1");
			}
		});
		JavaPairRDD<SecondarySortKey,String> paris_rdd = lines.mapToPair(new PairFunction<String, SecondarySortKey, String>() {
			private static final long serialVersionUID = 1L;
			SecondarySortKey ssk = null;
			public Tuple2<SecondarySortKey, String> call(String tuple)
					throws Exception {
				ssk = new SecondarySortKey(Integer.valueOf(tuple.split("\t")[4]),Integer.valueOf(tuple.split("\t")[3]));
				return new Tuple2<SecondarySortKey, String>(ssk,tuple);
			}
		});
		JavaPairRDD<SecondarySortKey,String > sorted_rdd = paris_rdd.sortByKey(false);
		JavaRDD<String>  sorted_lines = sorted_rdd.map(new Function<Tuple2<SecondarySortKey,String>, String>() {
			private static final long serialVersionUID = 1L;
			public String call(Tuple2<SecondarySortKey, String> v1)
					throws Exception {
				return v1._2;
			}
		});
		sorted_lines.foreach(new VoidFunction<String>() {
			private static final long serialVersionUID = 1L;
			public void call(String line) throws Exception {
				System.out.println(line);
			}
		});
		
		jsc.close();
		
	}
	@Override
	public boolean $greater(SecondarySortKey other) {
		if(this.tradeMoneys > other.getTradeMoneys()){
			return true;
		}else if(this.tradeMoneys == other.getTradeMoneys() && this.tradeHands > other.getTradeHands()){
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((tradeHands == null) ? 0 : tradeHands.hashCode());
		result = prime * result
				+ ((tradeMoneys == null) ? 0 : tradeMoneys.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SecondarySortKey other = (SecondarySortKey) obj;
		if (tradeHands == null) {
			if (other.tradeHands != null)
				return false;
		} else if (!tradeHands.equals(other.tradeHands))
			return false;
		if (tradeMoneys == null) {
			if (other.tradeMoneys != null)
				return false;
		} else if (!tradeMoneys.equals(other.tradeMoneys))
			return false;
		return true;
	}

	public Integer getTradeMoneys() {
		return tradeMoneys;
	}

	public void setTradeMoneys(Integer tradeMoneys) {
		this.tradeMoneys = tradeMoneys;
	}

	public Integer getTradeHands() {
		return tradeHands;
	}

	public void setTradeHands(Integer tradeHands) {
		this.tradeHands = tradeHands;
	}

	@Override
	public boolean $greater$eq(SecondarySortKey other) {
		if(this.$greater(other)){
			return true;
		}else if(this.tradeMoneys == other.getTradeMoneys() && this.tradeHands == other.getTradeHands()){
			return true;
		}
		return false;
	}

	@Override
	public boolean $less(SecondarySortKey other) {
		if(this.tradeMoneys < other.tradeMoneys ){
			return true;
		}else if(this.tradeMoneys == other.tradeMoneys && this.tradeHands < other.getTradeHands()){
			return true;
		}
		return false;
	}

	@Override
	public boolean $less$eq(SecondarySortKey other) {
		if(this.$less(other)){
			return true;
		}else if(this.tradeMoneys == other.getTradeMoneys() && this.tradeHands == other.getTradeHands()){
			return true;
		}
		return false;
	}

	@Override
	public int compare(SecondarySortKey other) {
		if(this.tradeMoneys - other.getTradeMoneys() != 0){
			return this.tradeMoneys - other.getTradeMoneys();
		}else {
			return this.tradeHands - other.getTradeHands();
		}
	}

	@Override
	public int compareTo(SecondarySortKey other) {
		if(this.tradeMoneys - other.getTradeMoneys() != 0){
			return this.tradeMoneys - other.getTradeMoneys();
		}else {
			return this.tradeHands - other.getTradeHands();
		}
	}
	public SecondarySortKey(Integer tradeMoneys, Integer tradeHands) {
		super();
		this.tradeMoneys = tradeMoneys;
		this.tradeHands = tradeHands;
	}
	public SecondarySortKey() {
		super();
	}
	
}
