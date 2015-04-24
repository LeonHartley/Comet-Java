
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
