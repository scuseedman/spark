/** 
 * Project Name:spark-java 
 * File Name:StockEntity.java 
 * Package Name:com.seed.entity 
 * Date:2018年3月14日下午3:54:47 
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
package com.seed.entity;

import java.io.Serializable;

/** 
 * ClassName:StockEntity
 * Date:     2018年3月14日 下午3:54:47 
 * DESC:	
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class StockEntity implements Comparable<StockEntity>,Serializable{
	private static final long serialVersionUID = 1L;
	private String trde_time;//成交时间
	private double trade_price;//成交价格
	private String trade_diff;//价格变动
	private Integer trade_hands;//成交手数
	private Integer trade_moneys;//成交额
	private String trade_type;//成交类型
	private static final String SPLIT = "\t";
	
	
	
	public StockEntity(String line) {
		super();
		this.trde_time = line.split(SPLIT)[0];
		this.trade_price = Double.valueOf(line.split(SPLIT)[1]);
		this.trade_diff = line.split(SPLIT)[2];
		this.trade_hands = Integer.valueOf(line.split(SPLIT)[3]);
		this.trade_moneys = Integer.valueOf(line.split(SPLIT)[4]);
		this.trade_type = line.split(SPLIT)[5];
		
	}




	public StockEntity() {
		super();
	}




	public StockEntity(String trde_time, double trade_price, String trade_diff,
			Integer trade_hands, Integer trade_moneys, String trade_type) {
		super();
		this.trde_time = trde_time;
		this.trade_price = trade_price;
		this.trade_diff = trade_diff;
		this.trade_hands = trade_hands;
		this.trade_moneys = trade_moneys;
		this.trade_type = trade_type;
	}




	public String getTrde_time() {
		return trde_time;
	}




	public void setTrde_time(String trde_time) {
		this.trde_time = trde_time;
	}




	public double getTrade_price() {
		return trade_price;
	}




	public void setTrade_price(double trade_price) {
		this.trade_price = trade_price;
	}




	public String getTrade_diff() {
		return trade_diff;
	}




	public void setTrade_diff(String trade_diff) {
		this.trade_diff = trade_diff;
	}




	public Integer getTrade_hands() {
		return trade_hands;
	}




	public void setTrade_hands(Integer trade_hands) {
		this.trade_hands = trade_hands;
	}




	public Integer getTrade_moneys() {
		return trade_moneys;
	}




	public void setTrade_moneys(Integer trade_moneys) {
		this.trade_moneys = trade_moneys;
	}




	public String getTrade_type() {
		return trade_type;
	}




	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}




	@Override
	public int compareTo(StockEntity other) {
		return - this.trade_hands - other.getTrade_hands();
	}




	@Override
	public String toString() {
		return "StockEntity [trde_time=" + trde_time + ", trade_price="
				+ trade_price + ", trade_diff=" + trade_diff + ", trade_hands="
				+ trade_hands + ", trade_moneys=" + trade_moneys
				+ ", trade_type=" + trade_type + "]";
	}
	
	
}
