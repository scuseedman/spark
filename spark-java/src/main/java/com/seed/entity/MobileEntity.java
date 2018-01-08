/** 
 * Project Name:spark-java 
 * File Name:MobileEntity.java 
 * Package Name:com.seed.entity 
 * Date:2018年1月8日下午4:58:50 
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
 * ClassName:MobileEntity
 * Date:     2018年1月8日 下午4:58:50 
 * DESC:	
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class MobileEntity {
	private String partner_no;
	private String comp_no;
	private String mac;
	private String mobile;
	public String getPartner_no() {
		return partner_no;
	}
	public void setPartner_no(String partner_no) {
		this.partner_no = partner_no;
	}
	public String getComp_no() {
		return comp_no;
	}
	public void setComp_no(String comp_no) {
		this.comp_no = comp_no;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public MobileEntity(String partner_no, String comp_no, String mac,
			String mobile) {
		super();
		this.partner_no = partner_no;
		this.comp_no = comp_no;
		this.mac = mac;
		this.mobile = mobile;
	}
	public MobileEntity() {
		super();
	}
	//基于指定数据构造对象
	public MobileEntity(String line) {
		this.partner_no = line.split("\t")[0];
		this.comp_no = line.split("\t")[1];
		this.mac = line.split("\t")[2];
		this.mobile = line.split("\t")[3];
	}
	//只返回两个值
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.partner_no).append("|");
		sb.append(this.mobile);
		return sb.toString();
	}
	
}
