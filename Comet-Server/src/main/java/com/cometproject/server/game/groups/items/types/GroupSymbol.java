package com.cometproject.server.game.groups.items.types;

import com.cometproject.api.game.groups.items.IGroupBadgeItem;

import java.sql.ResultSet;
import java.sql.SQLException;


public class GroupSymbol implements IGroupBadgeItem {
    private int id;
    private String valueA;
    private String valueB;

    public GroupSymbol(ResultSet data) throws SQLException {
        this.id = data.getInt("id");
        this.valueA = data.getString("firstvalue");
        this.valueB = data.getString("secondvalue");
    }

    public int getId() {
        return id;
    }

    @Override
    public String getFirstValue() {
        return valueA;
    }

    @Override
    public String getSecondValue() {
        return valueB;
    }
}
