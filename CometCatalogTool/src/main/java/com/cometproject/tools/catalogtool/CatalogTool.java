package com.cometproject.tools.catalogtool;

import java.sql.*;

/**
 * Created by Matty on 06/05/2014.
 */
public class CatalogTool {
    public static String catalogPagesTable = "catalog_pages_new";
    public static String catalogItemsTable = "catalog_items_new";
    public static String furnitureTable = "furniture_new";

    public static String furnitureTableOld = "furniture";

    public static void main(String[] args) {
        new CatalogTool();
    }

    private Connection sqlConnection;

    public CatalogTool() {
        if (!this.init()) {
            // handle
        }
    }

    private boolean init() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            this.sqlConnection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/cometsrv?user=root&password=");

            System.out.println("Creating new catalog pages table");

            this.sqlConnection.prepareStatement("DROP TABLE IF EXISTS `" + catalogPagesTable + "`;").execute();
            this.sqlConnection.prepareStatement("CREATE TABLE `" + catalogPagesTable + "` (\n" +
                    "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                    "  `parent_id` int(11) NOT NULL DEFAULT '-1',\n" +
                    "  `caption` varchar(100) NOT NULL,\n" +
                    "  `icon_color` int(11) NOT NULL DEFAULT '1',\n" +
                    "  `icon_image` int(11) NOT NULL DEFAULT '1',\n" +
                    "  `visible` enum('0','1') NOT NULL DEFAULT '1',\n" +
                    "  `enabled` enum('0','1') NOT NULL DEFAULT '1',\n" +
                    "  `min_rank` int(10) unsigned NOT NULL DEFAULT '1',\n" +
                    "  `club_only` enum('0','1') NOT NULL DEFAULT '0',\n" +
                    "  `order_num` int(11) NOT NULL,\n" +
                    "  `page_layout` enum('default_3x3','frontpage','spaces','recycler','recycler_info','recycler_prizes','trophies','plasto','marketplace','marketplace_own_items','pets','pets2','club_buy','club_gifts','guild_frontpage','spaces_new','guild_custom_furni','bots','petcustomization','roomads','badge_display') NOT NULL DEFAULT 'default_3x3',\n" +
                    "  `page_headline` text NOT NULL,\n" +
                    "  `page_teaser` text NOT NULL,\n" +
                    "  `page_special` text NOT NULL,\n" +
                    "  `page_text1` text NOT NULL,\n" +
                    "  `page_text2` text NOT NULL,\n" +
                    "  `min_sub` int(11) NOT NULL DEFAULT '0',\n" +
                    "  `page_text_details` text NOT NULL,\n" +
                    "  `page_text_teaser` text NOT NULL,\n" +
                    "  `vip_only` enum('1','0') NOT NULL DEFAULT '0',\n" +
                    "  `page_link_description` text NOT NULL,\n" +
                    "  `page_link_pagename` text NOT NULL,\n" +
                    "  PRIMARY KEY (`id`)\n" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=9053 DEFAULT CHARSET=latin1;").execute();

            System.out.println("Created new catalog pages table");
            System.out.println();

            System.out.println("Creating new catalog items table");
            this.sqlConnection.prepareStatement("DROP TABLE IF EXISTS `" + catalogItemsTable + "`;").execute();
            this.sqlConnection.prepareStatement("CREATE TABLE `" + catalogItemsTable + "` (\n" +
                    "  `id` int(100) unsigned NOT NULL,\n" +
                    "  `page_id` int(11) NOT NULL,\n" +
                    "  `item_ids` varchar(120) NOT NULL,\n" +
                    "  `catalog_name` varchar(100) NOT NULL,\n" +
                    "  `cost_credits` int(11) NOT NULL,\n" +
                    "  `cost_pixels` int(11) NOT NULL,\n" +
                    "  `cost_snow` int(11) NOT NULL DEFAULT '0',\n" +
                    "  `amount` int(11) NOT NULL,\n" +
                    "  `vip` enum('0','1','2') NOT NULL DEFAULT '0',\n" +
                    "  `achievement` int(4) unsigned NOT NULL DEFAULT '0',\n" +
                    "  `song_id` int(11) unsigned NOT NULL DEFAULT '0',\n" +
                    "  `limited_sells` int(11) NOT NULL DEFAULT '0',\n" +
                    "  `limited_stack` int(11) NOT NULL DEFAULT '0',\n" +
                    "  `offer_active` enum('0','1') NOT NULL DEFAULT '1',\n" +
                    "  `extradata` varchar(255) NOT NULL,\n" +
                    "  `badge_id` varchar(50) DEFAULT '',\n" +
                    "  PRIMARY KEY (`id`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=latin1;").execute();

            System.out.println("Created new catalog items table");
            System.out.println();

            System.out.println("Creating new furniture table");
            this.sqlConnection.prepareStatement("DROP TABLE IF EXISTS `" + furnitureTable + "`;").execute();
            this.sqlConnection.prepareStatement("CREATE TABLE `" + furnitureTable + "` (\n" +
                    "  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,\n" +
                    "  `public_name` varchar(100) NOT NULL COMMENT 'temp only',\n" +
                    "  `item_name` varchar(100) NOT NULL,\n" +
                    "  `type` enum('s','i','e','h','r','v') NOT NULL DEFAULT 's',\n" +
                    "  `width` int(11) NOT NULL DEFAULT '1',\n" +
                    "  `length` int(11) NOT NULL DEFAULT '1',\n" +
                    "  `stack_height` varchar(255) NOT NULL DEFAULT '1',\n" +
                    "  `can_stack` enum('0','1') NOT NULL DEFAULT '1',\n" +
                    "  `can_sit` enum('0','1') NOT NULL DEFAULT '0',\n" +
                    "  `is_walkable` enum('0','1') NOT NULL DEFAULT '0',\n" +
                    "  `sprite_id` int(11) NOT NULL,\n" +
                    "  `allow_recycle` enum('0','1') NOT NULL DEFAULT '1',\n" +
                    "  `allow_trade` enum('0','1') NOT NULL DEFAULT '1',\n" +
                    "  `allow_marketplace_sell` enum('0','1') NOT NULL DEFAULT '1',\n" +
                    "  `allow_gift` enum('0','1') NOT NULL DEFAULT '1',\n" +
                    "  `allow_inventory_stack` enum('0','1') NOT NULL DEFAULT '1',\n" +
                    "  `interaction_type` enum('default','gate','postit','roomeffect','dimmer','trophy','bed','scoreboard','vendingmachine','alert','onewaygate','loveshuffler','habbowheel','dice','bottle','teleport','rentals','pet','roller','water','ball','bb_red_gate','bb_green_gate','bb_yellow_gate','bb_puck','bb_blue_gate','bb_patch','bb_teleport','blue_score','green_score','red_score','yellow_score','fbgate','tagpole','banzaicounter','red_goal','blue_goal','yellow_goal','green_goal','wired','wf_trg_onsay','wf_act_saymsg','wf_trg_enterroom','wf_act_moveuser','wf_act_togglefurni','wf_trg_furnistate','wf_trg_onfurni','pressure_pad','wf_trg_offfurni','wf_trg_gameend','wf_trg_gamestart','wf_trg_timer','wf_act_givepoints','wf_trg_attime','wf_trg_atscore','wf_act_moverotate','rollerskate','stickiepole','wf_xtra_random','wf_cnd_trggrer_on_frn','wf_cnd_furnis_hv_avtrs','wf_act_matchfurni','wf_cnd_has_furni_on','puzzlebox','switch','wf_act_give_phx','wf_cnd_phx','conditionfurnihasfurni','mannequin','gld_item','pet19','pet18','pet17','pet16','pet15','pet14','pet13','pet12','pet11','pet10','pet9','pet8','pet7','pet6','pet5','pet4','pet3','pet2','pet1','pet0','gift','roombg','hopper','bot1','snowhill') NOT NULL DEFAULT 'default',\n" +
                    "  `interaction_modes_count` int(11) NOT NULL DEFAULT '1',\n" +
                    "  `vending_ids` varchar(100) NOT NULL DEFAULT '0',\n" +
                    "  `is_arrow` enum('0','1') NOT NULL DEFAULT '0',\n" +
                    "  `foot_figure` enum('0','1') NOT NULL DEFAULT '0',\n" +
                    "  `stack_multiplier` enum('0','1') NOT NULL DEFAULT '0',\n" +
                    "  `subscriber` enum('0','1') NOT NULL DEFAULT '0',\n" +
                    "  `effectid` int(11) NOT NULL DEFAULT '0',\n" +
                    "  `height_adjustable` varchar(100) NOT NULL DEFAULT '0',\n" +
                    "  PRIMARY KEY (`id`)\n" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=999888163 DEFAULT CHARSET=latin1;").execute();

            System.out.println("Created new furniture table");
            System.out.println();

            System.out.println("Loading furniture data");
            ResultSet fRs = this.sqlConnection.prepareStatement("SELECT * FROM " + furnitureTableOld).executeQuery();

            while (fRs.next()) {
                new FurnitureLayout(fRs);
            }

            fRs.close();

        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
