CREATE TABLE IF NOT EXISTS `server_permissions_ranks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL DEFAULT '0',
  `flood_bypass` enum('1','0') NOT NULL DEFAULT '0',
  `flood_time` int(11) NOT NULL DEFAULT '30',
  `disconnectable` enum('1','0') NOT NULL DEFAULT '1',
  `mod_tool` enum('1','0') NOT NULL DEFAULT '0',
  `room_kickable` enum('1','0') NOT NULL DEFAULT '1',
  `room_full_control` enum('1','0') NOT NULL DEFAULT '1',
  `room_bypass_mute` enum('1','0') NOT NULL DEFAULT '1',
  `room_filter_bypass` enum('1','0') NOT NULL DEFAULT '0',
  `room_ignorable` enum('1','0') NOT NULL DEFAULT '1',
  `messenger_staff_chat` enum('1','0') NOT NULL DEFAULT '0',
  `messenger_max_friends` int(11) NOT NULL DEFAULT '1100',
  `about_detailed` enum('1','0') NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

INSERT INTO `server_permissions_ranks` (`id`, `name`, `flood_bypass`, `flood_time`, `disconnectable`, `mod_tool`, `room_kickable`, `room_full_control`, `room_bypass_mute`, `room_filter_bypass`, `room_ignorable`, `messenger_staff_chat`, `messenger_max_friends`, `about_detailed`) VALUES
	(1, 'Player', '0', 30, '1', '0', '1', '1', '0', '0', '1', '0', 1100, '0'),
	(2, 'VIP', '0', 10, '1', '0', '1', '1', '0', '0', '1', '0', 2000, '0'),
	(7, 'Admin', '1', 0, '0', '1', '0', '1', '1', '1', '0', '1', 2000, '1');
