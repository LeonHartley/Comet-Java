package com.cometproject.server.game.players.components.types;

import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.storage.queries.player.PlayerDao;

import java.sql.ResultSet;
import java.sql.SQLException;


public class MessengerRequest {
    private int fromId;

    private String username, look, motto;

    public MessengerRequest(ResultSet data) throws SQLException {
        this.fromId = data.getInt("from_id");

        PlayerData playerData = PlayerDao.getDataById(this.fromId);

        if (playerData != null) {
            this.username = playerData.getUsername();
            this.look = playerData.getFigure();
            this.motto = playerData.getMotto();
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
