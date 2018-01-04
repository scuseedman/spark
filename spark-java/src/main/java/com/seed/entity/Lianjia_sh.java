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

import scala.math.Ordered;



/** 
 * ClassName:Lianjia_shanhai
 * Date:     2018年1月3日 上午10:48:20 
 * DESC:	
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class Lianjia_sh implements Ordered<Lianjia_sh>, Comparable<Lianjia_sh>{
	private String communityName;
	private String roomNum;
	private String squarts;
	private String Floor;
	private String Toward;
	private int totalPrice;
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
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
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
	public Lianjia_sh(String communityName, String roomNum,
			String squarts, String floor, String toward, int totalPrice,
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
	public Lianjia_sh() {
		super();
	}
	public Lianjia_sh(String line) {
		this.communityName = line.split(SPLIT)[0];
		this.roomNum = line.split(SPLIT)[1];
		this.squarts = line.split(SPLIT)[2];
		Floor = line.split(SPLIT)[3];
		Toward = line.split(SPLIT)[4];
		this.totalPrice = Integer.valueOf(line.split(SPLIT)[5]);
		this.unitPrice = line.split(SPLIT)[6];
		this.desc = line.split(SPLIT)[7];
	}
	@Override
	public int compareTo(Lianjia_sh other) {
		return other.getTotalPrice() - this.totalPrice;//价高排位高,sort排序这个对象的时候需要实现这个方法
	}
	@Override
	public boolean $greater(Lianjia_sh other) {//大于
		if(this.communityName.length() < other.getCommunityName().length()){
			return true;
		}else if(this.communityName.length() == other.getCommunityName().length() && this.totalPrice > other.totalPrice){
			return true;
		}
		return false;
	}
	@Override
	public boolean $greater$eq(Lianjia_sh other) {
		if(this.communityName.length() < other.getCommunityName().length()){
			return true;
		}else if(this.communityName.length() == other.getCommunityName().length() && this.totalPrice >= other.totalPrice){
			return true;
		}
		return false;
	}
	@Override
	public boolean $less(Lianjia_sh other) {
		if(this.communityName.length() > other.getCommunityName().length()){
			return true;
		}else if(this.communityName.length() == other.getCommunityName().length() && this.totalPrice < other.totalPrice){
			return true;
		}
		return false;
	}
	@Override
	public boolean $less$eq(Lianjia_sh other) {
		if(this.communityName.length() > other.getCommunityName().length()){
			return true;
		}else if(this.communityName.length() == other.getCommunityName().length() && this.totalPrice <= other.totalPrice){
			return true;
		}
		return false;
	}
	@Override
	public int compare(Lianjia_sh other) {
		return this.totalPrice - other.getTotalPrice();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Floor == null) ? 0 : Floor.hashCode());
		result = prime * result + ((Toward == null) ? 0 : Toward.hashCode());
		result = prime * result
				+ ((communityName == null) ? 0 : communityName.hashCode());
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + ((roomNum == null) ? 0 : roomNum.hashCode());
		result = prime * result + ((squarts == null) ? 0 : squarts.hashCode());
		result = prime * result + totalPrice;
		result = prime * result
				+ ((unitPrice == null) ? 0 : unitPrice.hashCode());
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
		Lianjia_sh other = (Lianjia_sh) obj;
		if (Floor == null) {
			if (other.Floor != null)
				return false;
		} else if (!Floor.equals(other.Floor))
			return false;
		if (Toward == null) {
			if (other.Toward != null)
				return false;
		} else if (!Toward.equals(other.Toward))
			return false;
		if (communityName == null) {
			if (other.communityName != null)
				return false;
		} else if (!communityName.equals(other.communityName))
			return false;
		if (desc == null) {
			if (other.desc != null)
				return false;
		} else if (!desc.equals(other.desc))
			return false;
		if (roomNum == null) {
			if (other.roomNum != null)
				return false;
		} else if (!roomNum.equals(other.roomNum))
			return false;
		if (squarts == null) {
			if (other.squarts != null)
				return false;
		} else if (!squarts.equals(other.squarts))
			return false;
		if (totalPrice != other.totalPrice)
			return false;
		if (unitPrice == null) {
			if (other.unitPrice != null)
				return false;
		} else if (!unitPrice.equals(other.unitPrice))
			return false;
		return true;
	}
	
	
}
