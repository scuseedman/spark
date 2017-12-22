/** 
 * Project Name:spark-spring 
 * File Name:SparkUpperServiceImpl.java 
 * Package Name:com.seed 
 * Date:2017年12月22日下午4:27:39 
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
package com.seed.service.impl;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;




import javax.annotation.Resource;




import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SaveMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.seed.entity.SparkUser;
import com.seed.service.ISparkUpperService;
/** 
 * ClassName:SparkUpperServiceImpl
 * Function: TODO ADD FUNCTION.  
 * Reason:   TODO ADD REASON. 
 * Date:     2017年12月22日 下午4:27:39 
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
@Service
public class SparkUpperServiceImpl implements ISparkUpperService{
	private Logger logger =Logger.getLogger(SparkUpperServiceImpl.class);

	@Value("${spark.master}")
	public String master ; // = "local"

	@Value("${spark.url}")
	public String url ;//= "jdbc:mysql://192.168.0.202:3306/spark?useUnicode=true&characterEncoding=UTF-8";

	@Value("${spark.table}")
	public String table ; //= "wc_test"

	@Value("${spark.username}")
	public String username ;// = "root";

	@Value("${spark.password}")
	public String password ;

	@Resource
	public SQLContext sqlContext;

	@Resource
	public JavaSparkContext sc;

	public Properties getConnectionProperties(){
	Properties connectionProperties = new Properties();
	connectionProperties.setProperty("dbtable",table);
	connectionProperties.setProperty("user",username);//数据库用户
	connectionProperties.setProperty("password",password); //数据库用户密码
	return connectionProperties;
	}

	public String query() {
	logger.info("=======================this url:"+this.url);
	logger.info("=======================this table:"+this.table);
	logger.info("=======================this master:"+this.master);
	logger.info("=======================this username:"+this.username);
	logger.info("=======================this password:"+this.password);

	DataFrame df = null;
	//以下数据库连接内容请使用实际配置地址代替
	df = sqlContext.read().jdbc(url,table, getConnectionProperties());
	df.registerTempTable(table);
	String result = sqlContext.sql("select * from testtable").javaRDD().collect().toString();
	logger.info("=====================spark mysql:"+result);
	return result;
	}

	public String queryByCon(){
	logger.info("=======================this url:"+this.url);
	logger.info("=======================this table:"+this.table);
	logger.info("=======================this master:"+this.master);
	logger.info("=======================this username:"+this.username);
	logger.info("=======================this password:"+this.password);

	DataFrame df = sqlContext.read().jdbc(url, table, new String[]{"password=000000"}, getConnectionProperties());
	String result = df.collectAsList().toString();
	logger.info("=====================spark mysql:"+result);
	return null;
	}

	public void add(){
	List<SparkUser> list = new ArrayList<SparkUser>();
	SparkUser us = new SparkUser();
	us.setUsername("kevin");
	us.setPassword("000000");
	list.add(us);
	SparkUser us2 = new SparkUser();
	us2.setUsername("Lisa");
	us2.setPassword("666666");
	list.add(us2);

	JavaRDD<SparkUser> personsRDD = sc.parallelize(list);
	DataFrame userDf = sqlContext.createDataFrame(personsRDD, SparkUser.class);
	userDf.write().mode(SaveMode.Append).jdbc(url, table, getConnectionProperties());
	}
}
