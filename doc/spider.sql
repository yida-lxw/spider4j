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

 Date: 02/05/2023 08:16:24
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
  `commentCount` int(11) NOT NULL DEFAULT '0',
  `pageUrl` varchar(512) DEFAULT NULL,
  `pageId` varchar(60) NOT NULL,
  `channel` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `pageId_idx` (`pageId`)
) ENGINE=InnoDB AUTO_INCREMENT=216355 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for news_all_stat
-- ----------------------------
DROP TABLE IF EXISTS `news_all_stat`;
CREATE TABLE `news_all_stat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pageId` text NOT NULL,
  `media` text NOT NULL,
  `channel` text NOT NULL,
  `title` text,
  `commentCount` int(11) NOT NULL,
  `pageUrl` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for news_channel_stat
-- ----------------------------
DROP TABLE IF EXISTS `news_channel_stat`;
CREATE TABLE `news_channel_stat` (
  `pageId` text NOT NULL,
  `media` text NOT NULL,
  `channel` text NOT NULL,
  `title` text,
  `commentCount` int(11) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pageUrl` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for news_day_stat
-- ----------------------------
DROP TABLE IF EXISTS `news_day_stat`;
CREATE TABLE `news_day_stat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pageId` text NOT NULL,
  `media` text NOT NULL,
  `channel` text NOT NULL,
  `title` text,
  `commentCount` int(11) NOT NULL,
  `pageUrl` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for news_media_stat
-- ----------------------------
DROP TABLE IF EXISTS `news_media_stat`;
CREATE TABLE `news_media_stat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pageId` text NOT NULL,
  `media` text NOT NULL,
  `channel` text NOT NULL,
  `title` text,
  `commentCount` int(11) NOT NULL,
  `pageUrl` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=202 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for news_month_stat
-- ----------------------------
DROP TABLE IF EXISTS `news_month_stat`;
CREATE TABLE `news_month_stat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pageId` text NOT NULL,
  `media` text NOT NULL,
  `channel` text NOT NULL,
  `title` text,
  `commentCount` int(11) NOT NULL,
  `pageUrl` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for news_year_stat
-- ----------------------------
DROP TABLE IF EXISTS `news_year_stat`;
CREATE TABLE `news_year_stat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pageId` text NOT NULL,
  `media` text NOT NULL,
  `channel` text NOT NULL,
  `title` text,
  `commentCount` int(11) NOT NULL,
  `pageUrl` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
