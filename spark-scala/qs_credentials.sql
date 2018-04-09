/*
Navicat MySQL Data Transfer

Source Server         : hadoop03
Source Server Version : 50173
Source Host           : hadoop03:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2018-04-09 15:31:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for qs_credentials
-- ----------------------------
DROP TABLE IF EXISTS `qs_credentials`;
CREATE TABLE `qs_credentials` (
  `qs_num` varchar(40) DEFAULT NULL COMMENT 'qs编号',
  `comp_name` varchar(200) DEFAULT NULL COMMENT '公司名称',
  `prod_name` varchar(200) DEFAULT NULL COMMENT '产品名称',
  `home_addr` varchar(200) DEFAULT NULL COMMENT '住所',
  `prod_addr` varchar(200) DEFAULT NULL COMMENT '生产地址',
  `test_type` varchar(90) DEFAULT NULL COMMENT '检验方式',
  `allow_date` varchar(30) DEFAULT NULL COMMENT '发证日期',
  `validity_date` varchar(30) DEFAULT NULL COMMENT '有效期',
  `allow_unit` varchar(200) DEFAULT NULL COMMENT '发证单位',
  `tb_id` int(11) DEFAULT NULL COMMENT '表中id',
  `c_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  UNIQUE KEY `qs_num` (`qs_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qs_credentials
-- ----------------------------
INSERT INTO `qs_credentials` VALUES ('QS3704 0901 1261', '枣庄市喜乐多食品有限公司', '罐头(果蔬罐头)', '山亭区水泉李腾公路北侧', '山亭区水泉李腾公路北侧', '自行检验', '2015-8-18', '2018-8-17', '枣庄市食品药品监督管理局', '3971', '2018-04-09 15:29:36');
