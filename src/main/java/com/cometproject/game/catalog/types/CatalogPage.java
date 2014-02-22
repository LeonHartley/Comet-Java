package com.cometproject.game.catalog.types;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class CatalogPage {
    private int id;
    private String caption;
    private int icon;
    private int colour;
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
    private boolean enabled;

    private Map<Integer, CatalogItem> items;

    public CatalogPage(ResultSet data, Map<Integer, CatalogItem> items) throws SQLException {

        this.id = data.getInt("id");
        this.caption = data.getString("caption");
        this.icon = data.getInt("icon_image");
        this.colour = data.getInt("icon_color");
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
        this.enabled = data.getString("enabled").equals("1");

        this.items = items;
    }

    public int getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }

    public int getIcon() {
        return icon;
    }

    public int getColour() {
        return colour;
    }

    public int getMinRank() {
        return minRank;
    }

    public String getTemplate() {
        return template;
    }

    public int getParentId() {
        return parentId;
    }

    public String getHeadline() {
        return headline;
    }

    public String getTeaser() {
        return teaser;
    }

    public String getSpecial() {
        return special;
    }

    public String getPageText1() {
        return pageText1;
    }

    public String getPageText2() {
        return pageText2;
    }

    public String getPageTextDetails() {
        return pageTextDetails;
    }

    public String getPageTextTeaser() {
        return pageTextTeaser;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Map<Integer, CatalogItem> getItems() {
        return items;
    }
}
