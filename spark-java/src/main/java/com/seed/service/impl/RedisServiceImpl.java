/** 
 * Project Name:spark-java 
 * File Name:RedisServiceImpl.java 
 * Package Name:com.seed.service.impl 
 * Date:2017年12月28日下午4:12:42 
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

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import scala.Tuple2;

/** 
 * ClassName:RedisServiceImpl
 * Date:     2017年12月28日 下午4:12:42 
 * DESC:	将sparkstreaming的计算结果写入redis中的service 实现类
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class RedisServiceImpl {
	private Jedis jedis;//非切片额客户端连接
    private JedisPool jedisPool;//非切片连接池
    private ShardedJedis shardedJedis;//切片额客户端连接
    private ShardedJedisPool shardedJedisPool;//切片连接池
    
    
	public RedisServiceImpl() {
		super();
	}

	public RedisServiceImpl(String redisHost) {
		initialPool(redisHost); 
        initialShardedPool(redisHost); 
        shardedJedis = shardedJedisPool.getResource(); 
        jedis = jedisPool.getResource(); 
	}

	/**
     * 初始化非切片池
     */
    private void initialPool(String redisHost) 
    { 
        // 池基本配置 
        JedisPoolConfig config = new JedisPoolConfig(); 
//        config.setMaxActive(20); 
        config.setMaxIdle(5); 
//        config.setMaxWait(1000l); 
        config.setTestOnBorrow(false); 
        
        jedisPool = new JedisPool(config,redisHost,6379);
    }
    
    /** 
     * 初始化切片池 
     */ 
    private void initialShardedPool(String redisHost) 
    { 
        // 池基本配置 
        JedisPoolConfig config = new JedisPoolConfig(); 
//        config.setMaxActive(20); 
        config.setMaxIdle(5); 
//        config.setMaxWait(1000l); 
        config.setTestOnBorrow(false); 
        // slave链接 
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>(); 
        shards.add(new JedisShardInfo(redisHost, 6379, "master")); 

        // 构造池 
        shardedJedisPool = new ShardedJedisPool(config, shards); 
    } 
	/**
	 * 将计算结果写入redis中.如果数据存在，则更新，否则新增
	 * @param wordcount
	 * @param redisHost 
	 */
	public void putRes2Redis(Tuple2<String, Integer> wordcount) {
		System.out.println(" >>>>>=================<<<<<");
		System.out.println("word >>>>>>> " + wordcount._1);
		if(! shardedJedis.exists(wordcount._1)){//false 数据不存在,新增记录
			shardedJedis.set(wordcount._1, wordcount._2 + "");
		}else{//数据存在,更新
			shardedJedis.set(wordcount._1, Integer.valueOf(shardedJedis.get(wordcount._1)) + wordcount._2 + "");
		}
	}


}
