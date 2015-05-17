/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50522
Source Host           : localhost:3306
Source Database       : easy

Target Server Type    : MYSQL
Target Server Version : 50522
File Encoding         : 65001

Date: 2015-05-17 19:51:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `loan_source_log`
-- ----------------------------
DROP TABLE IF EXISTS `loan_source_log`;
CREATE TABLE `loan_source_log` (
  `id` int(11) NOT NULL,
  `logInfo` varchar(500) DEFAULT NULL,
  `logDate` datetime DEFAULT NULL,
  `source` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `LOG_FK_SOURCE` (`source`),
  CONSTRAINT `LOG_FK_SOURCE` FOREIGN KEY (`source`) REFERENCES `loan_source` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of loan_source_log
-- ----------------------------
