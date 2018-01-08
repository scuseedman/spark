/** 
 * Project Name:spark-java 
 * File Name:PartnerEntity.java 
 * Package Name:com.seed.entity 
 * Date:2018年1月8日下午4:53:39 
 * Copyright (c) 2018;  All Rights Reserved. 
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

import java.io.Serializable;

/** 
 * ClassName:PartnerEntity
 * Date:     2018年1月8日 下午4:53:39 
 * DESC:	
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class PartnerEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final String SPLIT = "|";
	private String partner_no;
	private String comp_name;
	private String comp_addr;
	private String sc_pname;
	private String sc_name;
	private String comp_mobile;
	private String comp_phone;
	private Integer cate_pid;
	private Integer cat_id;
	private String comp_manager;
	private Integer sc_pid;
	private Integer sc_id;
	private Integer area_id;
	private String add_datetime;
	private String main_goods;
	public String getPartner_no() {
		return partner_no;
	}
	public void setPartner_no(String partner_no) {
		this.partner_no = partner_no;
	}
	public String getComp_name() {
		return comp_name;
	}
	public void setComp_name(String comp_name) {
		this.comp_name = comp_name;
	}
	public String getComp_addr() {
		return comp_addr;
	}
	public void setComp_addr(String comp_addr) {
		this.comp_addr = comp_addr;
	}
	public String getSc_pname() {
		return sc_pname;
	}
	public void setSc_pname(String sc_pname) {
		this.sc_pname = sc_pname;
	}
	public String getSc_name() {
		return sc_name;
	}
	public void setSc_name(String sc_name) {
		this.sc_name = sc_name;
	}
	public String getComp_mobile() {
		return comp_mobile;
	}
	public void setComp_mobile(String comp_mobile) {
		this.comp_mobile = comp_mobile;
	}
	public String getComp_phone() {
		return comp_phone;
	}
	public void setComp_phone(String comp_phone) {
		this.comp_phone = comp_phone;
	}
	public Integer getCate_pid() {
		return cate_pid;
	}
	public void setCate_pid(Integer cate_pid) {
		this.cate_pid = cate_pid;
	}
	public Integer getCat_id() {
		return cat_id;
	}
	public void setCat_id(Integer cat_id) {
		this.cat_id = cat_id;
	}
	public String getComp_manager() {
		return comp_manager;
	}
	public void setComp_manager(String comp_manager) {
		this.comp_manager = comp_manager;
	}
	public Integer getSc_pid() {
		return sc_pid;
	}
	public void setSc_pid(Integer sc_pid) {
		this.sc_pid = sc_pid;
	}
	public Integer getSc_id() {
		return sc_id;
	}
	public void setSc_id(Integer sc_id) {
		this.sc_id = sc_id;
	}
	public Integer getArea_id() {
		return area_id;
	}
	public void setArea_id(Integer area_id) {
		this.area_id = area_id;
	}
	public String getAdd_datetime() {
		return add_datetime;
	}
	public void setAdd_datetime(String add_datetime) {
		this.add_datetime = add_datetime;
	}
	public String getMain_goods() {
		return main_goods;
	}
	public void setMain_goods(String main_goods) {
		this.main_goods = main_goods;
	}
	public PartnerEntity(String partner_no, String comp_name, String comp_addr,
			String sc_pname, String sc_name, String comp_mobile,
			String comp_phone, Integer cate_pid, Integer cat_id,
			String comp_manager, Integer sc_pid, Integer sc_id,
			Integer area_id, String add_datetime, String main_goods) {
		super();
		this.partner_no = partner_no;
		this.comp_name = comp_name;
		this.comp_addr = comp_addr;
		this.sc_pname = sc_pname;
		this.sc_name = sc_name;
		this.comp_mobile = comp_mobile;
		this.comp_phone = comp_phone;
		this.cate_pid = cate_pid;
		this.cat_id = cat_id;
		this.comp_manager = comp_manager;
		this.sc_pid = sc_pid;
		this.sc_id = sc_id;
		this.area_id = area_id;
		this.add_datetime = add_datetime;
		this.main_goods = main_goods;
	}
	public PartnerEntity() {
		super();
	}
	//基于字符串构造对象
	public PartnerEntity(String line) {
		this.partner_no = line.split("\\|")[0];
		this.comp_name =  line.split("\\|")[1];
		this.comp_addr =  line.split("\\|")[2];
		this.sc_pname =  line.split("\\|")[3];
		this.sc_name = line.split("\\|")[4];
		this.comp_mobile = line.split("\\|")[5];
		this.comp_phone = line.split("\\|")[6];
		this.cate_pid = Integer.valueOf(line.split("\\|")[7]);
		this.cat_id = Integer.valueOf(line.split("\\|")[8]);
		this.comp_manager = line.split("\\|")[9];
		this.sc_pid = Integer.valueOf(line.split("\\|")[10]);
		this.sc_id = Integer.valueOf(line.split("\\|")[11]);
		this.area_id = Integer.valueOf(line.split("\\|")[12]);
		this.add_datetime = line.split("\\|")[13];
		this.main_goods = line.split("\\|")[14];
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.partner_no).append(SPLIT);
		sb.append(this.comp_name).append(SPLIT);
		sb.append(this.comp_addr).append(SPLIT);
		sb.append(this.sc_pname).append(SPLIT);
		sb.append(this.sc_name).append(SPLIT);
		sb.append(this.comp_mobile).append(SPLIT);
		sb.append(this.comp_phone).append(SPLIT);
		sb.append(this.cate_pid).append(SPLIT);
		sb.append(this.cat_id).append(SPLIT);
		sb.append(this.comp_manager).append(SPLIT);
		sb.append(this.sc_pid).append(SPLIT);
		sb.append(this.sc_id).append(SPLIT);
		sb.append(this.area_id).append(SPLIT);
		sb.append(this.add_datetime).append(SPLIT);
		sb.append(this.main_goods);
		return sb.toString();
	}
	
	
}
