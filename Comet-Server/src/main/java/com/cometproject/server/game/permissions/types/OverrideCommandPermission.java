package com.cometproject.server.game.permissions.types;

public class OverrideCommandPermission {
    private final String commandId;
    private final int playerId;
    private final boolean enabled;

    public OverrideCommandPermission(String commandId, int playerId, boolean enabled) {
        this.commandId = commandId;
        this.playerId = playerId;
        this.enabled = enabled;
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
