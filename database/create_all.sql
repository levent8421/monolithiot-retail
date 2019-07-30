-- MySQL dump 10.13  Distrib 5.6.37, for Linux (x86_64)
--
-- Host: rm-uf668ux4p0c3o3lj2.mysql.rds.aliyuncs.com    Database: db_retail
-- ------------------------------------------------------
-- Server version	5.6.16-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup
--

SET @@GLOBAL.GTID_PURGED='324b8bf8-350a-11e9-bcb8-506b4b47c1ac:1-23490928,
325df890-350a-11e9-bcb8-98039b46e328:1-1076897';

--
-- Table structure for table `t_address_index`
--

DROP TABLE IF EXISTS `t_address_index`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_address_index` (
                                   `id` int(10) NOT NULL AUTO_INCREMENT,
                                   `province` varchar(255) NOT NULL,
                                   `city` varchar(244) NOT NULL,
                                   `city_code` varchar(100) NOT NULL,
                                   `district` varchar(255) DEFAULT NULL,
                                   `district_code` varchar(100) DEFAULT NULL,
                                   `create_time` datetime NOT NULL,
                                   `update_time` datetime NOT NULL,
                                   `deleted` bit(1) NOT NULL,
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_admin`
--

DROP TABLE IF EXISTS `t_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_admin` (
                           `id` int(10) NOT NULL AUTO_INCREMENT,
                           `name` varchar(255) NOT NULL,
                           `password` varchar(255) NOT NULL,
                           `create_time` datetime NOT NULL,
                           `update_time` datetime NOT NULL,
                           `deleted` bit(1) NOT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_cash_application`
--

DROP TABLE IF EXISTS `t_cash_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_cash_application` (
                                      `id` int(10) NOT NULL AUTO_INCREMENT,
                                      `user_id` int(10) NOT NULL,
                                      `amount` int(6) NOT NULL,
                                      `trade_no` varchar(255) NOT NULL,
                                      `status` int(2) NOT NULL,
                                      `create_time` datetime NOT NULL,
                                      `update_time` datetime NOT NULL,
                                      `deleted` bit(1) NOT NULL,
                                      PRIMARY KEY (`id`),
                                      KEY `FK_pk_cash_application_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=194 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_commission_log`
--

DROP TABLE IF EXISTS `t_commission_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_commission_log` (
                                    `id` int(10) NOT NULL AUTO_INCREMENT,
                                    `order_id` int(10) NOT NULL,
                                    `promoter_id` int(10) NOT NULL,
                                    `amount` int(10) NOT NULL,
                                    `reason` int(2) NOT NULL,
                                    `status` int(2) NOT NULL,
                                    `create_time` datetime NOT NULL,
                                    `update_time` datetime NOT NULL,
                                    `deleted` bit(1) NOT NULL,
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3236 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_commodity`
--

DROP TABLE IF EXISTS `t_commodity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_commodity` (
                               `id` int(10) NOT NULL AUTO_INCREMENT,
                               `store_id` int(10) NOT NULL,
                               `buy_limit` int(4) DEFAULT NULL,
                               `name` varchar(255) NOT NULL,
                               `description` text,
                               `images` varchar(255) DEFAULT NULL,
                               `share_image` varchar(255) NOT NULL,
                               `original_price` int(6) DEFAULT NULL,
                               `current_price` int(6) DEFAULT NULL,
                               `stock_count` int(4) DEFAULT NULL,
                               `sale_count` int(4) DEFAULT NULL,
                               `commission_1` int(6) NOT NULL,
                               `commission_2` int(6) NOT NULL,
                               `commission_3` int(6) NOT NULL,
                               `need_appointment` bit(1) NOT NULL,
                               `buy_end_time` datetime DEFAULT NULL,
                               `appointment_start_time` datetime DEFAULT NULL,
                               `appointment_end_time` datetime DEFAULT NULL,
                               `status` int(2) NOT NULL,
                               `create_time` datetime NOT NULL,
                               `update_time` datetime NOT NULL,
                               `deleted` bit(1) NOT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_order`
--

DROP TABLE IF EXISTS `t_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_order` (
                           `id` int(10) NOT NULL AUTO_INCREMENT,
                           `user_id` int(10) NOT NULL,
                           `promoter_id` int(10) DEFAULT NULL,
                           `promoter_2_id` int(10) DEFAULT NULL,
                           `promoter_3_id` int(10) DEFAULT NULL,
                           `commodity_id` int(10) NOT NULL,
                           `quantity` int(3) NOT NULL,
                           `store_id` int(10) NOT NULL,
                           `amount` int(10) NOT NULL,
                           `offline_payment_amount` int(10) DEFAULT NULL,
                           `username` varchar(255) NOT NULL,
                           `phone` varchar(100) NOT NULL,
                           `sn` varchar(255) NOT NULL,
                           `trade_no` varchar(255) NOT NULL,
                           `commodity_name` varchar(255) NOT NULL,
                           `commodity_description` varchar(255) DEFAULT NULL,
                           `status` int(2) NOT NULL,
                           `appointment_stock_id` int(10) DEFAULT NULL,
                           `complete_time` datetime DEFAULT NULL,
                           `create_time` datetime NOT NULL,
                           `update_time` datetime NOT NULL,
                           `deleted` bit(1) NOT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2121 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_refund_log`
--

DROP TABLE IF EXISTS `t_refund_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_refund_log` (
                                `id` int(10) NOT NULL AUTO_INCREMENT,
                                `trade_no` varchar(255) NOT NULL,
                                `order_id` int(10) NOT NULL,
                                `order_amount` int(10) NOT NULL,
                                `refund_amount` int(10) NOT NULL,
                                `complete_time` datetime DEFAULT NULL,
                                `status` int(2) NOT NULL,
                                `create_time` datetime NOT NULL,
                                `update_time` datetime NOT NULL,
                                `deleted` bit(1) NOT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_stock`
--

DROP TABLE IF EXISTS `t_stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_stock` (
                           `id` int(10) NOT NULL AUTO_INCREMENT,
                           `commodity_id` int(10) NOT NULL,
                           `store_id` int(10) NOT NULL,
                           `action_date` date NOT NULL,
                           `day_of_week` int(1) NOT NULL,
                           `start_time` time NOT NULL,
                           `end_time` time NOT NULL,
                           `stock_count` int(5) NOT NULL,
                           `booked_count` int(5) NOT NULL,
                           `complete_count` int(5) NOT NULL,
                           `create_time` datetime NOT NULL,
                           `update_time` datetime NOT NULL,
                           `deleted` bit(1) NOT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1838 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_store`
--

DROP TABLE IF EXISTS `t_store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_store` (
                           `id` int(10) NOT NULL AUTO_INCREMENT,
                           `name` varchar(255) NOT NULL,
                           `keeper_name` varchar(255) NOT NULL,
                           `phone` varchar(255) NOT NULL,
                           `longitude` double DEFAULT NULL,
                           `latitude` double DEFAULT NULL,
                           `province` varchar(100) DEFAULT NULL,
                           `city` varchar(100) DEFAULT NULL,
                           `region` varchar(100) DEFAULT NULL,
                           `address` varchar(255) DEFAULT NULL,
                           `address_index_id` int(10) DEFAULT NULL,
                           `order_num` int(10) DEFAULT NULL,
                           `login_name` varchar(255) DEFAULT NULL,
                           `password` varchar(255) DEFAULT NULL,
                           `wx_open_id` varchar(255) DEFAULT NULL,
                           `create_time` datetime NOT NULL,
                           `update_time` datetime NOT NULL,
                           `deleted` bit(1) NOT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user` (
                          `id` int(10) NOT NULL AUTO_INCREMENT,
                          `nickname` varchar(255) DEFAULT NULL,
                          `name` varchar(255) DEFAULT NULL,
                          `phone` varchar(100) DEFAULT NULL,
                          `wx_open_id` varchar(255) DEFAULT NULL,
                          `wx_avatar` varchar(255) DEFAULT NULL,
                          `role` int(2) NOT NULL,
                          `position_longitude` varchar(255) DEFAULT NULL,
                          `position_latitude` varchar(255) DEFAULT NULL,
                          `position` varchar(255) DEFAULT NULL,
                          `direct_income` int(6) DEFAULT NULL,
                          `team_income` int(6) DEFAULT NULL,
                          `withdrawals` int(6) DEFAULT NULL,
                          `promo_code` varchar(255) DEFAULT NULL,
                          `team_header_level` int(2) DEFAULT NULL,
                          `leader_id` int(10) DEFAULT NULL,
                          `promoter_id` int(10) DEFAULT NULL,
                          `wx_token_json` mediumtext,
                          `promoter_phone` varchar(100) DEFAULT NULL,
                          `promoter_wx_no` varchar(255) DEFAULT NULL,
                          `promoter_name` varchar(255) DEFAULT NULL,
                          `create_time` datetime NOT NULL,
                          `update_time` datetime NOT NULL,
                          `deleted` bit(1) NOT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11578 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-07-30 16:06:36
