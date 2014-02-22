package com.cometproject.game.catalog.types;

import javolution.util.FastList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CatalogItem {
    private int id;
    private String itemId;
    private String displayName;
    private int costCredits;
    private int costActivityPoints;
    private int costOther;

    private int amount;
    private boolean vip;
    private List<Integer> items;

    private int limitedTotal;
    private int limitedSells;
    private boolean allowOffer;
    private String badgeId;

    public CatalogItem(ResultSet data) throws SQLException {
        this.id = data.getInt("id");
        this.itemId = data.getString("item_ids");
        this.displayName = data.getString("catalog_name");
        this.costCredits = data.getInt("cost_credits");
        this.costActivityPoints = data.getInt("cost_pixels");
        this.costOther = data.getInt("cost_snow");
        this.amount = data.getInt("amount");
        this.vip = data.getString("vip").equals("1");
        this.limitedTotal = data.getInt("limited_stack");
        this.limitedSells = data.getInt("limited_sells");
        this.allowOffer = data.getString("offer_active").equals("1");
        this.badgeId = data.getString("badge_id");

        this.items = new FastList<>();

        if(itemId.contains(",")) {
            String[] split = itemId.split(",");

            for(String str : split) {
                if(!str.equals("")) {
                    this.items.add(Integer.valueOf(str));
                }
            }
        } else {
            this.items.add(Integer.valueOf(this.itemId));
        }
    }

    public int getId() {
        return this.id;
    }

    public String getItemId() {
        return itemId;
    }

    public List<Integer> getItems() {
        return this.items;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getCostCredits() {
        return costCredits;
    }

    public int getCostActivityPoints() {
        return costActivityPoints;
    }

    public int getCostOther() {
        return costOther;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isVip() {
        return vip;
    }

    public int getLimitedTotal() {
        return this.limitedTotal;
    }

    public int getLimitedSells() {
        return this.limitedSells;
    }

    public boolean allowOffer() {
        return this.allowOffer;
    }

    public void increaseLimitedSells(int amount) {
        this.limitedSells += amount;
    }

    public boolean hasBadge() {
        return !(this.badgeId.isEmpty());
    }

    public String getBadgeId() {
        return this.badgeId;
    }
}
