package com.cometproject.server.game.players.components.types.inventory;

public class InventoryItemSnapshot {
    private long id;
    private int baseItemId;
    private String extraData;

    public InventoryItemSnapshot(long id, int baseItemId, String extraData) {
        this.id = id;
        this.baseItemId = baseItemId;
        this.extraData = extraData;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getBaseItemId() {
        return baseItemId;
    }

    public void setBaseItemId(int baseItemId) {
        this.baseItemId = baseItemId;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }
}
