/*
Navicat MySQL Data Transfer

Source Server         : hadoop03
Source Server Version : 50173
Source Host           : hadoop03:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2018-04-09 15:14:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for pharmacist_licensed
-- ----------------------------
DROP TABLE IF EXISTS `pharmacist_licensed`;
CREATE TABLE `pharmacist_licensed` (
  `name` varchar(30) DEFAULT NULL COMMENT '姓名',
  `regist_num` varchar(40) DEFAULT NULL COMMENT '注册编号',
  `license_area` varchar(30) DEFAULT NULL COMMENT '执业地区',
  `license_type` varchar(42) DEFAULT NULL COMMENT '执业类别',
  `license_range` varchar(30) DEFAULT NULL COMMENT '执业范围',
  `license_company` varchar(90) DEFAULT NULL COMMENT '执业单位',
  `validity_date` varchar(30) DEFAULT NULL COMMENT '有效期',
  `tb_id` int(11) DEFAULT NULL,
  `c_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  UNIQUE KEY `regist_num` (`regist_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
