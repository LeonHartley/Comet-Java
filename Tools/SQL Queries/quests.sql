# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.6.20)
# Database: cometserver
# Generation Time: 2015-01-13 16:04:22 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NO2TES, SQL_NOTES=0 */;


# Dump of table quests
# ------------------------------------------------------------

DROP TABLE IF EXISTS `quests`;

CREATE TABLE `quests` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `category` varchar(32) NOT NULL DEFAULT '',
  `series_number` int(11) NOT NULL DEFAULT '0',
  `goal_type` int(10) NOT NULL DEFAULT '0',
  `goal_data` int(10) unsigned NOT NULL DEFAULT '0',
  `name` varchar(32) NOT NULL DEFAULT '',
  `reward` int(11) NOT NULL DEFAULT '10',
  `data_bit` varchar(2) NOT NULL DEFAULT '',
  `enabled` enum('1','0') DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `quests` WRITE;
/*!40000 ALTER TABLE `quests` DISABLE KEYS */;

INSERT INTO `quests` (`id`, `category`, `series_number`, `goal_type`, `goal_data`, `name`, `reward`, `data_bit`, `enabled`)
VALUES
	(1,'room_builder',1,0,3,'MOVEITEM',10,'_2','1'),
	(2,'social',1,8,1,'ENTEROTHERSROOM',10,'_2','1'),
	(3,'identity',1,14,1,'CHANGEFIGURE',10,'_2','1'),
	(4,'explore',1,17,1936,'FINDLIFEGUARDTOWER',10,'_2','1'),
	(5,'room_builder',2,1,3,'ROTATEITEM',10,'','1'),
	(6,'room_builder',3,2,1,'PLACEITEM',10,'','1'),
	(7,'room_builder',4,3,1,'PICKUPITEM',10,'','1'),
	(8,'room_builder',5,4,2,'SWITCHSTATE',10,'','1'),
	(9,'room_builder',6,5,1,'STACKITEM',10,'','1'),
	(10,'room_builder',7,6,1,'PLACEFLOOR',10,'','1'),
	(11,'room_builder',8,7,1,'PLACEWALLPAPER',10,'_1','1'),
	(12,'identity',2,15,1,'CHANGEMOTTO',10,'','1'),
	(13,'identity',3,16,1,'WEARBADGE',10,'','1'),
	(14,'social',2,9,1,'CHATWITHSOMEONE',10,'','1'),
	(15,'social',3,10,1,'REQUESTFRIEND',10,'','1'),
	(16,'social',4,11,1,'GIVERESPECT',10,'','1'),
	(17,'social',5,12,1,'DANCE',10,'','1'),
	(18,'social',6,13,1,'WAVE',10,'','1'),
	(19,'explore',2,17,1948,'SWIM',10,'','1'),
	(20,'explore',3,17,1969,'FINDSURFBOARD',10,'','1'),
	(21,'explore',4,17,1956,'FINDBEETLE',10,'','1'),
	(22,'explore',5,17,1369,'FINDNEONFLOOR',10,'','1'),
	(23,'explore',6,17,1375,'FINDDISCOBALL',10,'','1'),
	(24,'explore',7,17,1019,'FINDJUKEBOX',10,'','1'),
	(25,'explore',8,17,2050,'FINDBBGATE',10,'','1'),
	(26,'explore',9,17,2040,'FINDBBTILE',10,'','1'),
	(27,'explore',10,17,2049,'FINDBBTELEPORT',10,'','1'),
	(28,'explore',11,17,2167,'FINDFREEZEGATE',10,'','1'),
	(29,'explore',12,17,2172,'FINDFREEZESCOREBOARD',10,'','1'),
	(30,'explore',13,17,2166,'FINDFREEZEEXITTILE',10,'','1'),
	(31,'explore',14,17,1413,'ICESKATE',10,'','1'),
	(32,'explore',15,17,2148,'FINDTAGPOLE',10,'','1'),
	(33,'explore',16,17,2199,'ROLLERSKATE',10,'','1');

/*!40000 ALTER TABLE `quests` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
