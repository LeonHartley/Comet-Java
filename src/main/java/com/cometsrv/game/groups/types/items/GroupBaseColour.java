package com.cometsrv.game.groups.types.items;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupBaseColour {
    public int id;
    public String colour;

    public GroupBaseColour(ResultSet data) throws SQLException {
        this.id = data.getInt("id");
        this.colour = data.getString("firstvalue");
    }
}
