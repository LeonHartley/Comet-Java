package com.cometproject.server.game.navigator.types;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Category {
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
