package com.cometproject.tools.catalogtool;

import com.cometproject.api.networking.messages.IMessageComposer;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getIconImage() {
        return iconImage;
    }

    public void setIconImage(int iconImage) {
        this.iconImage = iconImage;
    }

    public int getIconColour() {
        return iconColour;
    }

    public void setIconColour(int iconColour) {
        this.iconColour = iconColour;
    }

    public int getMinRank() {
        return minRank;
    }

    public void setMinRank(int minRank) {
        this.minRank = minRank;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getTeaser() {
        return teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getPageText1() {
        return pageText1;
    }

    public void setPageText1(String pageText1) {
        this.pageText1 = pageText1;
    }

    public String getPageText2() {
        return pageText2;
    }

    public void setPageText2(String pageText2) {
        this.pageText2 = pageText2;
    }

    public String getPageTextDetails() {
        return pageTextDetails;
    }

    public void setPageTextDetails(String pageTextDetails) {
        this.pageTextDetails = pageTextDetails;
    }

    public String getPageTextTeaser() {
        return pageTextTeaser;
    }

    public void setPageTextTeaser(String pageTextTeaser) {
        this.pageTextTeaser = pageTextTeaser;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getClubOnly() {
        return clubOnly;
    }

    public void setClubOnly(String clubOnly) {
        this.clubOnly = clubOnly;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public int getMinSub() {
        return minSub;
    }

    public void setMinSub(int minSub) {
        this.minSub = minSub;
    }

    public String getVipOnly() {
        return vipOnly;
    }

    public void setVipOnly(String vipOnly) {
        this.vipOnly = vipOnly;
    }

    public String getPagelinkDesc() {
        return pagelinkDesc;
    }

    public void setPagelinkDesc(String pagelinkDesc) {
        this.pagelinkDesc = pagelinkDesc;
    }

    public String getPagelinkName() {
        return pagelinkName;
    }

    public void setPagelinkName(String pagelinkName) {
        this.pagelinkName = pagelinkName;
    }
}
