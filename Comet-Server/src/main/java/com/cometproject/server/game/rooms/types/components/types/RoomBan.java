package com.cometproject.server.game.rooms.types.components.types;

public class RoomBan {
    private int playerId;
    private String playerName;
    private int expireTimestamp;

    private boolean isPermanent;

    public RoomBan(int playerId, String playerName, int expireTimestamp) {
        this.playerId = playerId;
        this.expireTimestamp = expireTimestamp;
        this.playerName = playerName;

        this.isPermanent = this.expireTimestamp == -1;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getExpireTimestamp() {
        return expireTimestamp;
    }

    public boolean isPermanent() {
        return isPermanent;
    }

    public String getPlayerName() {
        return playerName;
    }
}
