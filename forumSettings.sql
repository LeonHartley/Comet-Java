CREATE TABLE IF NOT EXISTS `group_forum_settings` (
  `group_id` int(11) NOT NULL DEFAULT '0',
  `read_permission` enum('EVERYBODY','MEMBERS','ADMINISTRATORS') NOT NULL DEFAULT 'EVERYBODY',
  `post_permission` enum('EVERYBODY','MEMBERS','ADMINISTRATORS','OWNER') NOT NULL DEFAULT 'EVERYBODY',
  `thread_permission` enum('EVERYBODY','MEMBERS','ADMINISTRATORS','OWNER') NOT NULL DEFAULT 'EVERYBODY',
  `moderate_permission` enum('ADMINISTRATORS','OWNER') NOT NULL DEFAULT 'ADMINISTRATORS',
  PRIMARY KEY (`group_id`),
  KEY `group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
