ALTER TABLE `players`
	CHANGE COLUMN `auth_ticket` `auth_ticket` VARCHAR(100) NULL;

DROP TABLE `navigator_featured_rooms`;

CREATE TABLE IF NOT EXISTS `navigator_publics` (
  `room_id` int(11) NOT NULL AUTO_INCREMENT,
  `caption` varchar(64) NOT NULL,
  `description` varchar(150) NOT NULL,
  `image_url` text NOT NULL,
  `order_num` int(11) NOT NULL DEFAULT '1',
  `enabled` enum('0','1') NOT NULL DEFAULT '1',
  PRIMARY KEY (`room_id`),
  KEY `ordernum` (`order_num`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `navigator_staff_picks` (
  `room_id` int(11) NOT NULL DEFAULT '0',
  `featured_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `navigator_categories`
(`id`, `category`, `category_identifier`, `public_name`, `category_type`, `search_allowance`, `order_id`)
	VALUES (38, 'official_view', 'official-root', 'Staff Picks', 'staff_picks', 'SHOW_MORE', 2);

UPDATE navigator_categories SET category_type = 'public' WHERE category_type = 'featured';

CREATE TABLE `items_wired_rewards` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`player_id` INT(11) NOT NULL,
	`item_id` BIGINT(20) NOT NULL,
	PRIMARY KEY (`id`),
	INDEX `item_id` (`item_id`)
) COLLATE='latin1_swedish_ci' ENGINE=InnoDB;
