ALTER TABLE `catalog_pages`
	ADD COLUMN `type` ENUM('DEFAULT','BUNDLE') NULL DEFAULT 'DEFAULT' AFTER `parent_id`,
	ADD COLUMN `extra_data` VARCHAR(50) NULL DEFAULT NULL AFTER `type`;

CREATE TABLE `room_bundles` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`enabled` ENUM('1','0') NOT NULL DEFAULT '1',
	`alias` VARCHAR(255) NULL DEFAULT 'roombundle',
	`room_id` INT(11) NULL DEFAULT NULL,
	`model_data` TEXT NULL,
	`bundle_data` TEXT NULL,
	`cost_credits` INT(11) NULL DEFAULT '20',
	`cost_seasonal` INT(11) NULL DEFAULT '0',
	`cost_vip` INT(11) NULL DEFAULT '0',
	PRIMARY KEY (`id`),
	UNIQUE INDEX `alias` (`alias`)
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB;

INSERT into server_locale (`key`, `value`) VALUES
	("command.bundle.name", "bundle"),
 	("command.bundle.description", "Used for managing bundles.");

INSERT into permission_commands (`command_id`, `minimum_rank`) VALUES("bundle_command", 7);
