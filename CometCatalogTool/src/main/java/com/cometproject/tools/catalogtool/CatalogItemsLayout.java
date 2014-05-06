package com.cometproject.tools.catalogtool;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Matty on 06/05/2014.
 */
public class CatalogItemsLayout {
    private int id;
    private int pageId;
    private String itemId;
    private String catalogName;
    private int costCredits;
    private int costActivityPoints;
    private int costSnow;

    private int amount;
    private String vip;

    private int achievement;
    private int songId;

    private int limitedStack;
    private int limitedSells;
    private String allowOffer;
    private String extraData;
    private String badgeId;

    public CatalogItemsLayout(ResultSet data) throws SQLException {
        this.id = data.getInt("id");
        this.pageId = data.getInt("page_id");
        this.itemId = data.getString("item_ids");
        this.catalogName = data.getString("catalog_name");
        this.costCredits = data.getInt("cost_credits");
        this.costActivityPoints = data.getInt("cost_pixels");
        this.costSnow = data.getInt("cost_snow");
        this.amount = data.getInt("amount");
        this.vip = data.getString("vip");
        this.achievement = data.getInt("achievement");
        this.songId = data.getInt("song_id");
        this.limitedStack = data.getInt("limited_stack");
        this.limitedSells = data.getInt("limited_sells");
        this.allowOffer = data.getString("offer_active");
        this.extraData = data.getString("extradata");
        this.badgeId = data.getString("badge_id");
    }
}
