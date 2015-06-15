package com.cometproject.server.game.navigator.types;

import com.cometproject.api.game.rooms.RoomCategory;

import java.sql.ResultSet;
import java.sql.SQLException;


public class Category implements RoomCategory {
    public static final int MISSING_CATEGORY_ID = 0;
    public static final String MISSING_CATEGORY_TITLE = "Missing category";
    public static final int MISSING_CATEGORY_RANK = 1;
    public static final boolean MISSING_CATEGORY_ALLOW_TRADE = true;

    public static final Category MISSING_CATEGORY = new Category();

    private int id;
    private String title;
    private int rank;
    private boolean allowTrade;

    public Category(ResultSet result) throws SQLException {
        this.id = result.getInt("id");
        this.title = result.getString("name");
        this.rank = result.getInt("min_rank");
        this.allowTrade = result.getString("can_trade").equals("1");
    }

    public Category() {
        this.id = MISSING_CATEGORY_ID;
        this.title = MISSING_CATEGORY_TITLE;
        this.rank = MISSING_CATEGORY_RANK;
        this.allowTrade = MISSING_CATEGORY_ALLOW_TRADE;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getRank() {
        return rank;
    }

    public boolean canTrade() {
        return allowTrade;
    }
}
