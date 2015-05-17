/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50522
Source Host           : localhost:3306
Source Database       : easy

Target Server Type    : MYSQL
Target Server Version : 50522
File Encoding         : 65001

Date: 2015-05-17 19:51:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `sign_client`
-- ----------------------------
DROP TABLE IF EXISTS `sign_client`;
CREATE TABLE `sign_client` (
  `id` int(11) NOT NULL,
  `signDate` date DEFAULT NULL,
  `clientCode` varchar(20) DEFAULT NULL,
  `client` bigint(20) DEFAULT NULL,
  `loanAmount` varchar(10) DEFAULT NULL,
  `loanType` varchar(20) DEFAULT NULL,
  `loanBank` varchar(100) DEFAULT NULL,
  `loanSource` varchar(20) DEFAULT NULL,
  `follower` bigint(20) DEFAULT NULL,
  `followDate` date DEFAULT NULL,
  `followInfo` varchar(100) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `backDate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `SIGN_CLIENT_FK_CLIENT` (`client`),
  KEY `SIGN_FOLLOW_FK_USER` (`follower`),
  CONSTRAINT `SIGN_FOLLOW_FK_USER` FOREIGN KEY (`follower`) REFERENCES `dgg_user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `SIGN_CLIENT_FK_CLIENT` FOREIGN KEY (`client`) REFERENCES `dgg_client` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

