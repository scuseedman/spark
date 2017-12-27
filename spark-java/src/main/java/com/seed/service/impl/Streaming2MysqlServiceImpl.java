/** 
 * Project Name:spark-java 
 * File Name:Streaming2MysqlServiceImpl.java 
 * Package Name:com.seed.service.impl 
 * Date:2017年12月27日下午3:04:04 
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;





import org.apache.log4j.Logger;

import com.seed.entity.WordCountEntity;
import com.seed.utils.ConnectorPools;

import scala.Tuple2;

/** 
 * ClassName:Streaming2MysqlServiceImpl
 * Date:     2017年12月27日 下午3:04:04 
 * DESC: service 层将处理结果写入msyql
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */

public class Streaming2MysqlServiceImpl {
	private static Logger logger = Logger.getLogger(Streaming2MysqlServiceImpl.class);
	/**
	 * 将结果写入mysql中
	 * @param wordcounts
	 */
	public void putWords2Mysql(Iterator<Tuple2<String, Integer>> wordcounts) {
		// TODO Auto-generated method stub
		logger.info("will put the data into table ===> ");
		try {
			Connection connection = ConnectorPools.getConnection();
			Tuple2<String,Integer> wordcount = null;
			String insert = "insert ignore into test.wc_res (word,count) values (?,?)";
			String select = "select word,count from test.wc_res where word=?";
			String update = "update test.wc_res set count=count+? where word=?";
			while(wordcounts.hasNext()){
				wordcount = wordcounts.next();
				WordCountEntity entity = new WordCountEntity();
				entity.setWord(wordcount._1);//word
				entity.setCount(wordcount._2);//count
				PreparedStatement pstmt = connection.prepareStatement(select);
				pstmt.setString(1, entity.getWord());
				ResultSet res = pstmt.executeQuery();
				System.out.println(wordcount._1 + " <====> " + wordcount._2);
				if(! res.next()){//记录不存在,则插入新记录
					System.out.println(" <============== not exists ==============> insert !!");
					pstmt = connection.prepareStatement(insert);
					pstmt.setString(1, wordcount._1);//word铁定进入库中 ^_^
					pstmt.setInt(2, wordcount._2);
					pstmt.executeUpdate();
				}else{//记录存在，则更新
					System.out.println("<<< =========== update");
					System.out.println(" ======> " + res.getString("word"));
					System.out.println(" ======> " + res.getInt("count") );
					System.out.println("=========== >>>");
					PreparedStatement pst = connection.prepareStatement(update);
					pst.setInt(1, wordcount._2);//count
					pst.setString(2, wordcount._1);//word
					pst.executeUpdate();
				}
				pstmt.close();
				res.close();
//				logger.info(wordcount._1 + " ===> " + wordcount._2);
			}
			ConnectorPools.returnConnection(connection);
			
		} catch (Exception e) {
			logger.warn(" ======> put the res into table failed , please check it  !!!!" ,e);
		}
	}
	
}
