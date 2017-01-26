package com.cometproject.server.game.permissions.types;

import java.sql.ResultSet;
import java.sql.SQLException;


public class OverrideCommandPermission {
    private String commandId;
    private int playerId;
    private boolean enabled;

    public OverrideCommandPermission(ResultSet data) throws SQLException {
        this.commandId = data.getString("command_id");
        this.playerId = data.getInt("player_id");
        this.enabled = data.getString("enabled").equals("1");
    }

    public String getCommandId() {
        return commandId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
