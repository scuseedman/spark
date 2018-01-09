/** 
 * Project Name:spark-java 
 * File Name:GroupCityEntity.java 
 * Package Name:com.seed.entity 
 * Date:2018年1月9日上午10:23:18 
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
 * ClassName:GroupCityEntity
 * Date:     2018年1月9日 上午10:23:18 
 * DESC:	
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class GroupCityEntity {
	private String mobile;
	private String city_name;
	private String area_name;
	private String comp_name;
	private static final String SPLIT = "\\|";
	private static final String S_SPLIT = "|";
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public String getArea_name() {
		return area_name;
	}
	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}
	public String getComp_name() {
		return comp_name;
	}
	public void setComp_name(String comp_name) {
		this.comp_name = comp_name;
	}
	public GroupCityEntity(String mobile, String city_name, String area_name,
			String comp_name) {
		super();
		this.mobile = mobile;
		this.city_name = city_name;
		this.area_name = area_name;
		this.comp_name = comp_name;
	}
	public GroupCityEntity() {
		super();
	}
	//定义构造器构造指定对象
	public GroupCityEntity(String line) {
		this.mobile = line.split(SPLIT)[15];
		this.city_name = line.split(SPLIT)[3];
		this.area_name = line.split(SPLIT)[4];
		this.comp_name = line.split(SPLIT)[1];
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.mobile).append(S_SPLIT);
		sb.append(this.city_name).append(S_SPLIT);
		sb.append(this.area_name).append(S_SPLIT);
		sb.append(this.comp_name);
		return sb.toString();
	}
	
	
}
