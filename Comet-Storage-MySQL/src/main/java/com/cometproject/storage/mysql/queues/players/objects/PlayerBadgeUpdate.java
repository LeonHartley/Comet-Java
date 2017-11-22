package com.cometproject.storage.mysql.queues.players.objects;

public class PlayerBadgeUpdate {

    private final int playerId;
    private final String badgeId;
    private final int slot;

    public PlayerBadgeUpdate(int playerId, String badgeId, int slot) {
        this.playerId = playerId;
        this.badgeId = badgeId;
        this.slot = slot;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getBadgeId() {
        return badgeId;
    }

    public int getSlot() {
        return slot;
    }
}
