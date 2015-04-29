package com.cometproject.tools.catalogtool;

import com.cometproject.api.networking.messages.IMessageComposer;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public int getCostCredits() {
        return costCredits;
    }

    public void setCostCredits(int costCredits) {
        this.costCredits = costCredits;
    }

    public int getCostActivityPoints() {
        return costActivityPoints;
    }

    public void setCostActivityPoints(int costActivityPoints) {
        this.costActivityPoints = costActivityPoints;
    }

    public int getCostSnow() {
        return costSnow;
    }

    public void setCostSnow(int costSnow) {
        this.costSnow = costSnow;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public int getAchievement() {
        return achievement;
    }

    public void setAchievement(int achievement) {
        this.achievement = achievement;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public int getLimitedStack() {
        return limitedStack;
    }

    public void setLimitedStack(int limitedStack) {
        this.limitedStack = limitedStack;
    }

    public int getLimitedSells() {
        return limitedSells;
    }

    public void setLimitedSells(int limitedSells) {
        this.limitedSells = limitedSells;
    }

    public String getAllowOffer() {
        return allowOffer;
    }

    public void setAllowOffer(String allowOffer) {
        this.allowOffer = allowOffer;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public String getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(String badgeId) {
        this.badgeId = badgeId;
    }
}
