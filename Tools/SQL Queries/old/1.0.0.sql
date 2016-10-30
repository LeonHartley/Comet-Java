INSERT INTO `permission_commands` (`command_id`, `minimum_rank`, `vip_only`) VALUES ('notification_command', 7, '0');

INSERT INTO `server_locale` (`key`, `value`) VALUES ('command.notification.name', 'notification'), ('command.notification.description', '<image> <message>');

INSERT INTO `permission_commands` (`command_id`, `minimum_rank`, `vip_only`) VALUES ('notification_command', 7, '0');

INSERT INTO `server_locale` (`key`, `value`) VALUES ('command.notification.name', 'notification'), ('command.notification.description', '<image> <message>');

/** Only run if you wanna queue player data updates, reminder: if you edit data when a player is online and then they do something that requires their data to be saved then immediately logged out, the data will be overwritten. */
INSERT into `server_config` (`key`, `value`) VALUES('comet.data.playerDataStorageQueue', 'true');

