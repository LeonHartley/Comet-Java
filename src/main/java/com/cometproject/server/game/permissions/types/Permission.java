package com.cometproject.server.game.permissions.types;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Permission {
    private String id;
    private int minRank;

    public Permission(ResultSet perm) throws SQLException {
        this.id = perm.getString("fuse");
        this.minRank = perm.getInt("min_rank");
    }

    public String getId() {
        return this.id;
    }

    public int getRank() {
        return this.minRank;
    }
}
