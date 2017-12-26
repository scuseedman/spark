package com.seed.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;

import org.apache.commons.dbcp.DriverConnectionFactory;

import com.seed.config.Global;

/**
 * 手动创建mysql数据库连接池
 * @author lwd
 *
 */
public class ConnectorPools {
	private static LinkedList<Connection> connectQueue;
	static{
		try {
			Class.forName(Global.getConfVal("driverClassName"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	//获取连接，多线程获取连接
	public synchronized static Connection getConnection(){
		try{
			if(connectQueue == null){
				connectQueue = new LinkedList<Connection>();
				for(int i = 0; i <= 5 ; i ++){
					Connection connection = DriverManager.getConnection(
							Global.getConfVal("jdbc_url"), 
							Global.getConfVal("jdbc_username"),
							Global.getConfVal("jdbc_password"));
					connectQueue.push(connection);
				}
			}
		}catch( Exception e){
			e.printStackTrace();
		}
		return connectQueue.poll();//取出队列中的第一个出来 ，取出连接来用
	}
	
	public static void returnConnection(Connection conn){
		connectQueue.push(conn);//将连接归队
	}
}
