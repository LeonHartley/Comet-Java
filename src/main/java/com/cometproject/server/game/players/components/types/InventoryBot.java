package com.cometproject.server.game.players.components.types;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryBot {
    private int id, ownerId;
    private String name, figure, gender, motto;

    public InventoryBot(ResultSet data) throws SQLException {
        this.id = data.getInt("id");
        this.ownerId = data.getInt("owner_id");

        this.name = data.getString("name");
        this.figure = data.getString("figure");
        this.gender = data.getString("gender");
        this.motto = data.getString("motto");
    }

    public int getId() {
        return this.id;
    }

    public int getOwnerId() {
        return this.ownerId;
    }

    public String getName() {
        return this.name;
    }

    public String getFigure() {
        return this.figure;
    }

    public String getGender() {
        return this.gender;
    }

    public String getMotto() {
        return this.motto;
    }
}
