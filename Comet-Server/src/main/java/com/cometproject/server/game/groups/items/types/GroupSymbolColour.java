package com.cometproject.server.game.groups.items.types;

import com.cometproject.api.game.groups.items.IGroupBadgeItem;

import java.sql.ResultSet;
import java.sql.SQLException;


public class GroupSymbolColour implements IGroupBadgeItem {
    private int id;
    private String colour;

    public GroupSymbolColour(ResultSet data) throws SQLException {
        this.id = data.getInt("id");
        this.colour = data.getString("firstvalue");
    }

    public int getId() {
        return id;
    }

    @Override
    public String getFirstValue() {
        return colour;
    }

    @Override
    public String getSecondValue() {
        return null;
    }
}
