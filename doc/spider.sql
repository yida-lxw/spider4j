/*
 Navicat MySQL Data Transfer

 Source Server         : Local MySQL 5
 Source Server Type    : MySQL
 Source Server Version : 50737
 Source Host           : localhost:3306
 Source Schema         : spider

 Target Server Type    : MySQL
 Target Server Version : 50737
 File Encoding         : 65001

 Date: 01/05/2023 08:23:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `media` varchar(20) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `content` text,
  `publishDate` datetime DEFAULT NULL,
  `commentCount` int(11) NOT NULL,
  `pageUrl` varchar(512) DEFAULT NULL,
  `pageId` varchar(60) DEFAULT NULL,
  `channel` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3279 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for news_all_stat
-- ----------------------------
DROP TABLE IF EXISTS `news_all_stat`;
CREATE TABLE `news_all_stat` (
  `pageId` text,
  `media` text,
  `channel` text,
  `title` text,
  `commentCount` int(11) DEFAULT NULL,
  `id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for news_channel_stat
-- ----------------------------
DROP TABLE IF EXISTS `news_channel_stat`;
CREATE TABLE `news_channel_stat` (
  `pageId` text,
  `media` text,
  `channel` text,
  `title` text,
  `commentCount` int(11) DEFAULT NULL,
  `id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for news_media_stat
-- ----------------------------
DROP TABLE IF EXISTS `news_media_stat`;
CREATE TABLE `news_media_stat` (
  `pageId` text,
  `media` text,
  `channel` text,
  `title` text,
  `commentCount` int(11) DEFAULT NULL,
  `id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
