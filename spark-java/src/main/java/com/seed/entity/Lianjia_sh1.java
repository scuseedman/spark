/** 
 * Project Name:spark-java 
 * File Name:Lianjia_shanhai.java 
 * Package Name:com.seed.entity 
 * Date:2018年1月3日上午10:48:20 
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
package com.seed.entity;



/** 
 * ClassName:Lianjia_shanhai
 * Date:     2018年1月3日 上午10:48:20 
 * DESC:	
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class Lianjia_sh1 {
	private String communityName;
	private String roomNum;
	private String squarts;
	private String Floor;
	private String Toward;
	private String totalPrice;
	private String unitPrice;
	private String desc ;
	private static final String SPLIT = ",";
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	public String getRoomNum() {
		return roomNum;
	}
	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}
	public String getSquarts() {
		return squarts;
	}
	public void setSquarts(String squarts) {
		this.squarts = squarts;
	}
	public String getFloor() {
		return Floor;
	}
	public void setFloor(String floor) {
		Floor = floor;
	}
	public String getToward() {
		return Toward;
	}
	public void setToward(String toward) {
		Toward = toward;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Lianjia_sh1(String communityName, String roomNum,
			String squarts, String floor, String toward, String totalPrice,
			String unitPrice, String desc) {
		super();
		this.communityName = communityName;
		this.roomNum = roomNum;
		this.squarts = squarts;
		Floor = floor;
		Toward = toward;
		this.totalPrice = totalPrice;
		this.unitPrice = unitPrice;
		this.desc = desc;
	}
	public Lianjia_sh1() {
		super();
	}
	public Lianjia_sh1(String line) {
		this.communityName = line.split(SPLIT)[0];
		this.roomNum = line.split(SPLIT)[1];
		this.squarts = line.split(SPLIT)[2];
		Floor = line.split(SPLIT)[3];
		Toward = line.split(SPLIT)[4];
		this.totalPrice =line.split(SPLIT)[5];
		this.unitPrice = line.split(SPLIT)[6];
		this.desc = line.split(SPLIT)[7];
	}
	
	
}
