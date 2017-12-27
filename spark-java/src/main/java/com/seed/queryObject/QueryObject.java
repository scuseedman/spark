/** 
 * Project Name:spark-java 
 * File Name:QueryObject.java 
 * Package Name:com.seed.queryObject 
 * Date:2017年12月27日下午5:06:28 
 * DESC:
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
package com.seed.queryObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.seed.entity.WordCountEntity;
import com.seed.utils.ConnectorPools;

/** 
 * ClassName:QueryObject
 * Date:     2017年12月27日 下午5:06:28 
 * desc: 对查询结果进行对象封装
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class QueryObject {
	public static void main(String[] args) {
		WordCountEntity entity = (WordCountEntity)getResFromMysql("select count,word from test.wc_res where word=?",WordCountEntity.class);
		System.out.println(" ===> " + entity.getWord());
		System.out.println(" ===> " + entity.getCount());
	}

	private static Object getResFromMysql(String sql,Class clazz) {
		Connection connection = ConnectorPools.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			ps.setString(1, "zhangfei");
			rs = ps.executeQuery();
//			if(rs.next()) //一旦调用.next方法，将会取出里面的结果而将只有一条记录的结果集变成空集合
//				System.out.println("查询结果 ===> " + rs.getString(1) + " >>> " + rs.getInt(2));
			ResultSetMetaData rsmd =  rs.getMetaData();
			String[] colNames = new String[rsmd.getColumnCount()];
			System.out.println(" length of the colNames ===> " + colNames.length);
			for(int i = 1; i <= colNames.length; i ++){//获取所有的columns 
				colNames[i-1] = rsmd.getColumnLabel(i);
				System.out.println("colNames[" + (i -1) + " ] = " +rsmd.getColumnLabel(i));
			}
			Object obj = null;
			Method[] methods = clazz.getMethods();//利用反射获取该类的所有方法
			if (rs.next()) {
				obj = clazz.newInstance();
				for (int i = 0; i < colNames.length; i++) {//两个循环交叉判断
					String colName = colNames[i];
					System.out.println("colName >>> " + colName);
					String methodName = "set" + colName.substring(0,1).toUpperCase()+colName.substring(1);
					for (Method m : methods) {
						System.out.println("Method ===> " + m.getName());
						if (methodName.equals(m.getName())) {
							m.invoke(obj, rs.getObject(colName));
						}
					}

				}
			}
			return obj;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			ConnectorPools.returnConnection(connection);
		}
		
		
		return null;
	}
}
