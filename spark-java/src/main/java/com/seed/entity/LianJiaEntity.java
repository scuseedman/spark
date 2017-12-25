/** 
 * Project Name:spark-java 
 * File Name:LianJiaEntity.java 
 * Package Name:com.seed.entity 
 * Date:2017年12月25日上午11:24:03 
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
package com.seed.entity;
/** 
 * ClassName:LianJiaEntity
 * Date:     2017年12月25日 上午11:24:03 
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class LianJiaEntity {
	private String houseName;//小区名
	private String roomNum;//几房
	private String squares;//平方数
	private String face;//朝向
	private String otherInfo;//其他信息
	private String isLift;//是否电梯房
	private String totalPrice;//总价
	private String pricePerM;//单价
	private static final String SPLIT = "|";
	private static final String OR_SPLIT = ",";
	
	public LianJiaEntity(String line) {
		this.houseName = line.split(OR_SPLIT)[0];
		this.roomNum = line.split(OR_SPLIT)[1];
		this.squares = line.split(OR_SPLIT)[2];
		this.face = line.split(OR_SPLIT)[3];
		this.otherInfo = line.split(OR_SPLIT)[4];
		this.isLift = line.split(OR_SPLIT)[5].replaceAll("电梯", "");
		this.totalPrice = line.split(OR_SPLIT)[6].replaceAll("万", "0000");
		this.pricePerM = line.split(OR_SPLIT)[7];
	}
	
	public LianJiaEntity() {
		super();
	}

	public LianJiaEntity(String houseName, String roomNum, String squares,
			String face, String otherInfo, String isLift, String totalPrice,
			String pricePerM) {
		super();
		this.houseName = houseName;
		this.roomNum = roomNum;
		this.squares = squares;
		this.face = face;
		this.otherInfo = otherInfo;
		this.isLift = isLift;
		this.totalPrice = totalPrice;
		this.pricePerM = pricePerM;
	}

	public String getHouseName() {
		return houseName;
	}
	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}
	public String getRoomNum() {
		return roomNum;
	}
	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}
	public String getSquares() {
		return squares;
	}
	public void setSquares(String squares) {
		this.squares = squares;
	}
	public String getFace() {
		return face;
	}
	public void setFace(String face) {
		this.face = face;
	}
	public String getOtherInfo() {
		return otherInfo;
	}
	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}
	public String getIsLift() {
		return isLift;
	}
	public void setIsLift(String isLift) {
		this.isLift = isLift;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getPricePerM() {
		return pricePerM;
	}
	public void setPricePerM(String pricePerM) {
		this.pricePerM = pricePerM;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(houseName).append(SPLIT);
		sb.append(roomNum).append(SPLIT);
		sb.append(squares).append(SPLIT);
		sb.append(face).append(SPLIT);
		sb.append(otherInfo).append(SPLIT);
		sb.append(isLift).append(SPLIT);
		sb.append(totalPrice).append(SPLIT);
		sb.append(pricePerM);
		return sb.toString();
	}
	
}
