package com.cometproject.server.game.groups.types.items;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupSymbol {
    public int id;
    public String valueA;
    public String valueB;

    public GroupSymbol(ResultSet data) throws SQLException {
        this.id = data.getInt("id");
        this.valueA = data.getString("firstvalue");
        this.valueB = data.getString("secondvalue");
    }
}
