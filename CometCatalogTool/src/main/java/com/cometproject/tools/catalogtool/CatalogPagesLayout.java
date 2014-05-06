package com.cometproject.tools.catalogtool;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Matty on 06/05/2014.
 */
public class CatalogPagesLayout {
    private int id;
    private String caption;
    private int iconImage;
    private int iconColour;
    private int minRank;
    private String template;
    private int parentId;

    private String headline;
    private String teaser;
    private String special;
    private String pageText1;
    private String pageText2;
    private String pageTextDetails;
    private String pageTextTeaser;
    private String enabled;

    private String visible;
    private String clubOnly;
    private int orderNum;
    private int minSub;
    private String vipOnly;

    private String pagelinkDesc;
    private String pagelinkName;

    public CatalogPagesLayout(ResultSet data) throws SQLException {

        this.id = data.getInt("id");
        this.caption = data.getString("caption");
        this.iconImage = data.getInt("icon_image");
        this.iconColour = data.getInt("icon_color");
        this.minRank = data.getInt("min_rank");
        this.template = data.getString("page_layout");
        this.parentId = data.getInt("parent_id");

        this.headline = data.getString("page_headline");
        this.teaser = data.getString("page_teaser");
        this.special = data.getString("page_special");
        this.pageText1 = data.getString("page_text1");
        this.pageText2 = data.getString("page_text2");
        this.pageTextDetails = data.getString("page_text_details");
        this.pageTextTeaser = data.getString("page_text_teaser");
        this.enabled = data.getString("enabled");

        this.visible = data.getString("visible");
        this.clubOnly = data.getString("club_only");
        this.orderNum = data.getInt("order_num");
        this.minSub = data.getInt("min_sub");
        this.vipOnly = data.getString("vip_only");

        this.pagelinkDesc = data.getString("page_link_description");
        this.pagelinkName = data.getString("page_link_pagename");
    }
}
