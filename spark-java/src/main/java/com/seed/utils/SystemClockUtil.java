/** 
 * Project Name:jsonParseBySpark 
 * File Name:SystemClockUtil.java 
 * Package Name:com.formax.utils 
 * Date:2018年5月25日下午4:23:49 
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
package com.seed.utils;

import java.sql.Timestamp;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
/** 
 * ClassName:SystemClockUtil
 * Date:     2018年5月25日 下午4:23:49 
 * DESC:	解决多并发情况下多次调用 System.currentTimeMillis性能消耗问题
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class SystemClockUtil {
	private final long period;
	
	private final AtomicLong now;
	
	private SystemClockUtil(long period) {
		this.period = period;
		this.now = new AtomicLong(System.currentTimeMillis());
		scheduleClockUpdating();
	}
	
	private static SystemClockUtil instance() {
		return InstanceHolder.INSTANCE;
	}
	
	public static long now() {
		return instance().currentTimeMillis();
	}
	
	public static String nowDate() {
		return new Timestamp(instance().currentTimeMillis()).toString();
	}
	
	private void scheduleClockUpdating() {
		
		ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
			
			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r, "System Clock");
				thread.setDaemon(true);
				return thread;
			}
		});
		
		scheduler.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				now.set(System.currentTimeMillis());
			}
		}, period, period, TimeUnit.MILLISECONDS);
	}
	
	private long currentTimeMillis() {
		return now.get();
	}
	
	private static class InstanceHolder {
		public static final SystemClockUtil INSTANCE = new SystemClockUtil(1);
	}
}
