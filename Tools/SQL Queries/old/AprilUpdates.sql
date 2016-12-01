
/* 24/04/2015 */
ALTER TABLE `rooms`
	ADD COLUMN `disabled_commands` VARCHAR(255) NOT NULL DEFAULT '';

INSERT INTO `server_locale` (`key`, `value`) VALUES ('command.disablecommand.name', 'disablecommand');
INSERT INTO `server_locale` (`key`, `value`) VALUES ('command.disablecommand.description', 'Disables a command for all players in the room.');
INSERT INTO `server_locale` (`key`, `value`) VALUES ('command.disablecommand.success', 'The command was disabled successfully.');
INSERT INTO `server_locale` (`key`, `value`) VALUES ('command.disablecommand.error', 'This command cannot be disabled!');

INSERT INTO `server_locale` (`key`, `value`) VALUES ('command.enablecommand.name', 'enablecommand');
INSERT INTO `server_locale` (`key`, `value`) VALUES ('command.enablecommand.description', 'Enables a command that was disabled previously.');
INSERT INTO `server_locale` (`key`, `value`) VALUES ('command.enablecommand.success', 'The command was enabled successfully.');
INSERT INTO `server_locale` (`key`, `value`) VALUES ('command.enablecommand.error', 'This command cannot be enabled!');

INSERT INTO `permission_commands` (`command_id`, `vip_only`) VALUES ('enablecommand_command', '0');
INSERT INTO `permission_commands` (`command_id`, `vip_only`) VALUES ('disablecommand_command', '0');

INSERT INTO `server_locale` (`key`, `value`) VALUES ('command.disabled', 'This command is disabled in this room!');

/* 25/04/2015 */
CREATE TABLE `furniture_music` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `title` varchar(64) NOT NULL,
  `artist` varchar(32) NOT NULL,
  `song_data` text NOT NULL,
  `length` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of furniture_music
-- ----------------------------
INSERT INTO `furniture_music` VALUES ('1', 'xmas_2011', 'Xmas Magic', 'Silent Aurora', '1:387,4;387,3;0,5;387,4;0,23;558,8;0,8;558,8;0,8;558,4;0,4;558,4;0,4;558,4;476,1;0,3;558,4;0,3;476,2;0,2;476,2;0,14;387,6:2:0,4;484,8;484,3;0,5;61,4;0,40;481,1;482,3;481,5;482,3;481,5;482,3;481,5;482,3;481,4;0,1;482,4;0,3;476,2;0,2;476,4:3:0,4;477,2;476,2;0,2;476,2;0,2;476,2;0,3;476,4;0,1;485,4;0,43;485,4;0,7;476,3;0,2;485,4;0,1;476,4;0,5;558,4:4:0,2;485,6;0,11;485,7;0,6;498,78;0,2;473,4;474,4;473,4:5:0,16;326,12;326,3;0,1;326,68;326,2:6:0,8;476,2;0,2;476,2;0,2;476,3;0,6;325,42;0,12;325,12;0,4;325,4:7:0,27;325,3;325,2;0,14;475,1;0,1;475,4;475,4;0,22;477,2;0,5;476,4;476,3:8:0,38;474,2;0,30;486,32:meta,1;c,1', '256');
INSERT INTO `furniture_music` VALUES ('2', 'who_dares_stacks', 'Who Dares Stacks', 'Rage Against the Fuse', '1:255,4;310,7;0,9;310,14;0,4:2:0,3;309,1;308,7;309,1;308,7;309,1;308,7;309,1;308,7;309,1;0,2:3:0,4;162,7;0,1;162,7;0,1;162,15;0,3:4:0,6;135,4;135,4;135,4;0,2;135,4;135,4;135,4;135,4;0,2:5:0,14;155,2;0,2;155,2;314,1;315,1;314,1;315,1;314,1;315,1;314,1;0,1;314,1;315,1;314,1;315,1;314,1;315,1;314,1;315,1;0,2:6:0,12;311,1;0,1;311,1;0,1;311,1;0,1;311,1;0,1;311,1;0,5;155,2;311,1;0,1;311,1;0,1;311,1;0,3;311,1;0,1:', '76');
INSERT INTO `furniture_music` VALUES ('3', 'electric_pixels', 'Electric Pixels', 'Habbo de Gaia', '1:73,36;435,40;565,2;566,2;468,24;0,2:2:0,1;76,2;43,1;0,1;76,2;0,2;76,2;43,1;0,1;76,2;0,2;76,2;0,21;439,8;454,2;0,2;454,2;0,2;454,20;0,6;420,2;0,2;420,2;0,2;420,2;0,2;420,2;0,2;97,2;0,6:3:0,2;76,2;0,2;76,2;0,2;76,2;0,2;76,2;0,2;76,2;0,16;73,30;434,4;420,2;434,6;435,24;0,4:4:0,1;80,16;0,19;463,6;438,2;463,2;438,2;463,2;438,2;0,2;97,2;0,4;79,2;0,2;79,2;42,6;79,2;420,2;0,4;447,20;73,2;0,4:5:52,34;97,2;0,2;420,2;0,2;420,2;0,2;420,2;439,4;0,4;456,22;0,2;456,20;0,6:6:0,7;42,6;0,1;42,6;565,2;566,2;565,2;566,2;565,2;566,2;565,2;566,2;468,38;0,2;73,4;463,2;0,2;439,4;463,2;0,2;439,4;463,2;0,8:7:0,12;577,24;52,62;0,8:8:77,20;553,4;554,4;553,4;554,4;460,32;447,10;0,2;460,18;0,8:', '212');
INSERT INTO `furniture_music` VALUES ('4', 'galactic_disco', 'Galactic Disco', 'DJ Bobba feat. Habboway', '1:214,2;95,20;0,2;260,1;0,3;260,1;0,1;407,16;95,6;0,6:2:0,14;461,2;0,2;461,2;0,10;495,2;0,2;495,2;0,2;495,2;0,2;495,2;0,2;412,2;0,5;495,2;0,3:3:0,2;413,28;458,14;0,2;86,6;0,6:4:0,2;411,42;0,2;256,1;257,5;0,6:5:0,6;465,2;0,2;465,2;0,2;465,2;0,2;465,2;0,2;531,2;0,2;531,2;0,6;461,2;0,2;461,2;0,2;461,2;0,4;92,1;93,4;0,5:6:0,6;72,2;0,2;72,2;0,2;72,2;0,2;72,2;0,2;72,2;0,1;72,3;0,2;72,2;0,2;72,2;0,2;72,2;0,2;72,2;0,4;72,2;0,8:', '116');
INSERT INTO `furniture_music` VALUES ('5', 'lost_my_tapes_at_goa', 'Tapes from Goa', 'Habnosis', '1:0,116;116,8;0,8;5,56;0,64:2:0,49;42,6;0,21;575,4;0,4;575,20;0,4;0,2;290,6;290,2;290,4;293,2;297,1;291,2;0,12;42,6;0,31;75,4;0,6;97,2;0,16;575,4;0,2;502,4;0,12;502,4;0,4;502,4:3:0,42;471,4;0,3;81,4;44,2;0,12;97,2;2,2;0,2;2,2;0,2;2,8;0,1;81,3;0,4;410,14;0,2;410,30;97,2;0,2;410,2;0,2;410,2;0,2;410,2;0,2;410,2;0,10;97,2;0,6;97,2;410,22;97,2;410,16:4:0,44;410,40;157,66;97,2;157,2;97,2;0,4;157,79:5:0,20;577,8;0,8;570,16;79,2;423,2;79,2;423,2;79,2;423,2;79,2;423,2;79,2;423,2;79,2;423,2;79,2;0,2;79,2;97,2;0,6;97,2;0,6;97,2;0,6;97,2;0,14;97,2;101,1;0,1;101,1;0,1;101,1;0,1;101,1;0,1;101,1;0,1;101,1;0,1;101,1;0,1;101,1;0,1;101,1;0,1;101,1;0,1;101,1;100,1;101,1;100,1;101,1;100,1;101,1;100,1;101,1;100,1;101,1;100,1;101,19;100,5;101,1;100,1;101,1;100,6;101,1;100,1;101,1;100,5;101,1;100,1;101,1;0,4;576,4;575,12:6:0,11;576,16;0,5;100,19;101,1;447,32;0,6;447,50;454,36;447,56:7:0,4;575,16;0,6;514,4;0,1;118,1;0,5;118,1;0,3;118,1;97,2;0,6;97,2;0,8;71,24;0,8;471,48;0,24;73,82:8:578,20;0,4;579,8;0,6;73,46;0,4;73,87;97,2;0,44;578,32;579,4:', '512');

ALTER TABLE `furniture`
	ADD COLUMN `song_id` INT(11) NOT NULL DEFAULT '0';

INSERT INTO `server_locale`
 (`key`, `value`) VALUES ('game.room.jukeboxExists', 'This room already has a jukebox!');
