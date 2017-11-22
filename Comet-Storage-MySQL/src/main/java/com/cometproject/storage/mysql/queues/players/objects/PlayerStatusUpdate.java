package com.cometproject.storage.mysql.queues.players.objects;

public class PlayerStatusUpdate {
    private final int playerId;
    private final boolean playerOnline;
    private final String ipAddress;

    public PlayerStatusUpdate(int playerId, boolean playerOnline, String ipAddress) {
        this.playerId = playerId;
        this.playerOnline = playerOnline;
        this.ipAddress = ipAddress;
    }

    public int getPlayerId() {
        return playerId;
    }

    public boolean isPlayerOnline() {
        return playerOnline;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }
}
