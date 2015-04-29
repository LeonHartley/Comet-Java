package com.cometproject.tools.catalogtool;

import com.cometproject.api.networking.messages.IMessageComposer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;


/**
 * Created by Matty on 06/05/2014.
 */
public class CatalogTool {
    public static String catalogPagesTable = "catalog_pages_new";
    public static String catalogItemsTable = "catalog_items_new";
    public static String furnitureTable = "furniture_new";

    public static String furnitureTableOld = "furniture";
    public static String catalogPagesTableOld = "catalog_pages";
    public static String catalogItemsTableOld = "catalog_items";

    public static void main(String[] args) {
        new CatalogTool();
    }

    private Connection sqlConnection;

    public CatalogTool() {
        if (!this.init()) {
            // handle
        }
    }

    private HashSet<FurnitureLayout> furniLayouts = new LinkedHashSet<>();
    private HashSet<CatalogPagesLayout> catalogPagesLayouts = new LinkedHashSet<>();
    private HashSet<CatalogItemsLayout> catalogItemsLayouts = new LinkedHashSet<>();

    private static FurnitureLayout curr;

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
                    ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;").execute();

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
                    "  `foot_figure` enum('0','1') NOT NULL DEFAULT '0',\n" +
                    "  `stack_multiplier` enum('0','1') NOT NULL DEFAULT '0',\n" +
                    "  `subscriber` enum('0','1') NOT NULL DEFAULT '0',\n" +
                    "  `effectid` int(11) NOT NULL DEFAULT '0',\n" +
                    "  `height_adjustable` varchar(100) NOT NULL DEFAULT '0',\n" +
                    "  `revision` int(11) NOT NULL DEFAULT '1',\n" +
                    "  PRIMARY KEY (`id`)\n" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;").execute();

            System.out.println("Created new furniture table");
            System.out.println();

            System.out.println("Loading furniture data");
            ResultSet fRs = this.sqlConnection.prepareStatement("SELECT * FROM " + furnitureTableOld).executeQuery();

            int furniCount = 0;

            while (fRs.next()) {
                furniCount++;
                this.furniLayouts.add(new FurnitureLayout(fRs));
            }

            fRs.close();

            System.out.println("Loaded a total of " + furniCount + " furniture!");
            System.out.println();

            System.out.println("Loading catalog pages data");
            ResultSet catalogPagesRs = this.sqlConnection.prepareStatement("SELECT * FROM " + catalogPagesTableOld).executeQuery();

            int cpCount = 0;

            while (catalogPagesRs.next()) {
                cpCount++;
                this.catalogPagesLayouts.add(new CatalogPagesLayout(catalogPagesRs));
            }

            fRs.close();

            System.out.println("Loaded a total of " + cpCount + " catalog pages!");
            System.out.println();

            System.out.println("Loading catalog items data");
            ResultSet catalogItemsRs = this.sqlConnection.prepareStatement("SELECT * FROM " + catalogItemsTableOld).executeQuery();

            int ciCount = 0;

            while (catalogItemsRs.next()) {
                ciCount++;
                this.catalogItemsLayouts.add(new CatalogItemsLayout(catalogItemsRs));
            }

            fRs.close();

            System.out.println("Loaded a total of " + ciCount + " items!");
            System.out.println();


            System.out.println("Sorting furniture and corresponding catalog item ids...");

            int newFurnitureLayoutId = 0;

            for (FurnitureLayout furnitureLayout : this.furniLayouts) {
                newFurnitureLayoutId++; // increase id
                int oldId = furnitureLayout.getId(); // store old id

                furnitureLayout.setId(newFurnitureLayoutId); // set new id

                // update all corresponding catalog items
                for (CatalogItemsLayout catalogItemsLayout : this.catalogItemsLayouts) {

                    if(catalogItemsLayout.getItemId().contains(",")) {
                        System.out.println("!!!!! item has , !!!!!!!");
                    }

                    Integer id = Integer.parseInt(catalogItemsLayout.getItemId());

                    if (id == oldId) {
                        catalogItemsLayout.setItemId(Integer.toString(newFurnitureLayoutId));
                    }
                }
            }

            System.out.println("Furniture has been re-organised!");
            System.out.println();

            System.out.println("Sorting catalog page ids");

            Map<Integer, Integer> parentIds = new HashMap<>();

            int newCatalogPageId = 0;

            for (CatalogPagesLayout catalogPagesLayout : this.catalogPagesLayouts) {
                newCatalogPageId++;
                int oldId = catalogPagesLayout.getId();

                catalogPagesLayout.setId(newCatalogPageId);

                // update all corresponding catalog item page ids
                for (CatalogItemsLayout catalogItemsLayout : this.catalogItemsLayouts) {
                    int pageId = catalogItemsLayout.getPageId();

                    if (pageId == oldId) {
                        catalogItemsLayout.setPageId(newCatalogPageId);
                    }
                }

                // save old id to new for parent updates
                parentIds.put(oldId, newCatalogPageId);
            }

            // Update parents

            for (CatalogPagesLayout catalogPagesLayout : this.catalogPagesLayouts) {
                if (parentIds.containsKey(catalogPagesLayout.getParentId())) { // do we need update parent ids?
                    int newId = parentIds.get(catalogPagesLayout.getParentId());
                    catalogPagesLayout.setParentId(newId);
                }
            }

            System.out.println("Catalog pages has been re-organised!");
            System.out.println();

            System.out.println("Inserting data into new tables..");

            for (FurnitureLayout furnitureLayout : this.furniLayouts) {
                curr = furnitureLayout;

                PreparedStatement statement = this.sqlConnection.prepareStatement("INSERT IGNORE INTO `" + furnitureTable + "` (id,public_name,item_name,type,width,length,stack_height,can_stack,can_sit,is_walkable,sprite_id,allow_recycle,allow_trade,allow_marketplace_sell,allow_gift,allow_inventory_stack,interaction_type,interaction_modes_count,vending_ids,foot_figure,stack_multiplier,subscriber,effectid,height_adjustable,revision) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
                statement.setInt(1, furnitureLayout.getId());
                statement.setString(2, furnitureLayout.getPublicName());
                statement.setString(3, furnitureLayout.getItemName());
                statement.setString(4, furnitureLayout.getType());
                statement.setInt(5, furnitureLayout.getWidth());
                statement.setInt(6, furnitureLayout.getLength());
                statement.setString(7, furnitureLayout.getStackMultiplier());
                statement.setString(8, furnitureLayout.getCanStack());
                statement.setString(9, furnitureLayout.getCanSit());
                statement.setString(10, furnitureLayout.getCanWalk());
                statement.setInt(11, furnitureLayout.getSpriteId());
                statement.setString(12, furnitureLayout.getAllowRecycle());
                statement.setString(13, furnitureLayout.getCanTrade());
                statement.setString(14, furnitureLayout.getAllowMarketplaceSell());
                statement.setString(15, furnitureLayout.getAllowGift());
                statement.setString(16, furnitureLayout.getCanInventoryStack());
                statement.setString(17, furnitureLayout.getInteraction());
                statement.setInt(18, furnitureLayout.getInteractionCycleCount());
                statement.setString(19, furnitureLayout.getVendingIds());
                statement.setString(20, furnitureLayout.getFootFigure());
                statement.setString(21, furnitureLayout.getStackMultiplier());
                statement.setString(22, furnitureLayout.getSubscriber());
                statement.setInt(23, furnitureLayout.getEffectId());
                statement.setString(24, furnitureLayout.getHeightAdjustable());
                statement.setInt(25, furnitureLayout.getRevision());
                statement.execute();
            }

            System.out.println("Furniture inserted!");
            System.out.println();

            System.out.println("Inserting catalog pages!");

            for (CatalogPagesLayout pagesLayout : this.catalogPagesLayouts) {
                PreparedStatement statement = this.sqlConnection.prepareStatement("INSERT IGNORE INTO `" + catalogPagesTable + "` (id,parent_id,caption,icon_color,icon_image,visible,enabled,min_rank,club_only,order_num,page_layout,page_headline,page_teaser,page_special,page_text1,page_text2,min_sub,page_text_details,page_text_teaser,vip_only,page_link_description,page_link_pagename) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
                statement.setInt(1, pagesLayout.getId());
                statement.setInt(2, pagesLayout.getParentId());
                statement.setString(3, pagesLayout.getCaption());
                statement.setInt(4, pagesLayout.getIconColour());
                statement.setInt(5, pagesLayout.getIconImage());
                statement.setString(6, pagesLayout.getVisible());
                statement.setString(7, pagesLayout.getEnabled());
                statement.setInt(8, pagesLayout.getMinRank());
                statement.setString(9, pagesLayout.getClubOnly());
                statement.setInt(10, pagesLayout.getOrderNum());
                statement.setString(11, pagesLayout.getTemplate());
                statement.setString(12, pagesLayout.getHeadline());
                statement.setString(13, pagesLayout.getTeaser());
                statement.setString(14, pagesLayout.getSpecial());
                statement.setString(15, pagesLayout.getPageText1());
                statement.setString(16, pagesLayout.getPageText2());
                statement.setInt(17, pagesLayout.getMinSub());
                statement.setString(18, pagesLayout.getPageTextDetails());
                statement.setString(19, pagesLayout.getPageTextTeaser());
                statement.setString(20, pagesLayout.getVipOnly());
                statement.setString(21, pagesLayout.getPagelinkDesc());
                statement.setString(22, pagesLayout.getPagelinkName());
                statement.execute();
            }

            System.out.println("Catalog pages inserted!");
            System.out.println();

            System.out.println("Inserting catalog items!");

            for (CatalogItemsLayout catalogItemsLayout : this.catalogItemsLayouts) {
                PreparedStatement statement = this.sqlConnection.prepareStatement("INSERT IGNORE INTO `" + catalogItemsTable + "` (id,page_id,item_ids,catalog_name,cost_credits,cost_pixels,cost_snow,amount,vip,achievement,song_id,limited_sells,limited_stack,offer_active,extradata,badge_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
                statement.setInt(1, catalogItemsLayout.getId());
                statement.setInt(2, catalogItemsLayout.getPageId());
                statement.setString(3, catalogItemsLayout.getItemId());
                statement.setString(4, catalogItemsLayout.getCatalogName());
                statement.setInt(5, catalogItemsLayout.getCostCredits());
                statement.setInt(6, catalogItemsLayout.getCostActivityPoints());
                statement.setInt(7, catalogItemsLayout.getCostSnow());
                statement.setInt(8, catalogItemsLayout.getAmount());
                statement.setString(9, catalogItemsLayout.getVip());
                statement.setInt(10, catalogItemsLayout.getAchievement());
                statement.setInt(11, catalogItemsLayout.getSongId());
                statement.setInt(12, catalogItemsLayout.getLimitedSells());
                statement.setInt(13, catalogItemsLayout.getLimitedStack());
                statement.setString(14, catalogItemsLayout.getAllowOffer());
                statement.setString(15, catalogItemsLayout.getExtraData());
                statement.setString(16, catalogItemsLayout.getBadgeId());
                statement.execute();
            }

            System.out.println("Catalog items inserted!");
            System.out.println();

            System.out.println("Catalog items, pages and furniture has been sorted!");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ID : " + curr.getId() + " INTERACTION : " + curr.getInteraction());
            return false;
        }

        return true;
    }
}
