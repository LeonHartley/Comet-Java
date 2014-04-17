package com.cometproject.server.game.players.components.types;

import com.cometproject.server.boot.Comet;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessengerRequest {
    private int fromId;

    private String username, look, motto;

    public MessengerRequest(ResultSet data) throws SQLException {
        this.fromId = data.getInt("from_id");

        ResultSet user = Comet.getServer().getStorage().getRow("SELECT `username`, `figure`, `motto` FROM `players` WHERE `id` = " + fromId);

        if (user != null) {
            this.username = user.getString("username");
            this.look = user.getString("figure");
            this.motto = user.getString("motto");
        }
    }

    public MessengerRequest(int id, String username, String figure, String motto) {
        this.fromId = id;
        this.username = username;
        this.look = figure;
        this.motto = motto;
    }

    public int getFromId() {
        return this.fromId;
    }

    public String getUsername() {
        return this.username;
    }

    public String getLook() {
        return this.look;
    }

    public String getMotto() {
        return this.motto;
    }
}
