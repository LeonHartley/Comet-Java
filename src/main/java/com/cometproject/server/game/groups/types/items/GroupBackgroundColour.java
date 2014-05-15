package com.cometproject.server.game.groups.types.items;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupBackgroundColour {
    public int id;
    public String colour;

    public GroupBackgroundColour(ResultSet data) throws SQLException {
        this.id = data.getInt("id");
        this.colour = data.getString("firstvalue");
    }
}
