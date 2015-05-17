/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50522
Source Host           : localhost:3306
Source Database       : easy

Target Server Type    : MYSQL
Target Server Version : 50522
File Encoding         : 65001

Date: 2015-05-17 19:51:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `loan_source`
-- ----------------------------
DROP TABLE IF EXISTS `loan_source`;
CREATE TABLE `loan_source` (
  `id` int(11) NOT NULL,
  `loanDate` date DEFAULT NULL COMMENT '放款时间',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `loanYear` varchar(10) DEFAULT NULL COMMENT '放款年限',
  `loanInterest` varchar(10) DEFAULT NULL COMMENT '放款利息',
  `serviceFee` varchar(20) DEFAULT NULL COMMENT '服务费',
  `interestType` int(11) DEFAULT NULL COMMENT '利息类型',
  `loanAmount` varchar(20) DEFAULT NULL COMMENT '放款金额',
  `receiveAmount` varchar(20) DEFAULT NULL COMMENT '收款金额',
  `signClient` int(11) DEFAULT NULL,
  `sourceName` varchar(20) DEFAULT NULL COMMENT '渠道名字',
  `sourceAmount` varchar(20) DEFAULT NULL COMMENT '渠道贷款金额',
  PRIMARY KEY (`id`),
  KEY `SOURCE_FK_SIGN` (`signClient`),
  CONSTRAINT `SOURCE_FK_SIGN` FOREIGN KEY (`signClient`) REFERENCES `sign_client` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of loan_source
-- ----------------------------
