package com.cometproject.server.game.rooms.items.types.floor.wired;

import com.cometproject.server.game.rooms.items.RoomItemFloor;

public class WiredItemSnapshot {
    private int itemId;
    private int x;
    private int y;
    private double z;
    private int rotation;
    private String extraData;

    public WiredItemSnapshot(RoomItemFloor floorItem) {
        this.itemId = floorItem.getId();
        this.x = floorItem.getX();
        this.y = floorItem.getY();
        this.z = floorItem.getHeight();
        this.rotation = floorItem.getRotation();
        this.extraData = floorItem.getExtraData();
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

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
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

    public interface Refreshable {
        void refreshSnapshots();
    }
}
