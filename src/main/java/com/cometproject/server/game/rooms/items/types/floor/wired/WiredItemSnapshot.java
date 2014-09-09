package com.cometproject.server.game.rooms.items.types.floor.wired;

public class WiredItemSnapshot {
    private int itemId;
    private int x;
    private int y;
    private int z;
    private int rotation;
    private String extraData;

    public WiredItemSnapshot(int itemId, int x, int y, int z, int rotation, String extraData) {
        this.itemId = itemId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotation = rotation;
        this.extraData = extraData;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }
}
