package com.cometproject.api.game.rooms.models;

public class CustomFloorMapData {
    private final int doorX, doorY, doorRotation, wallHeight;
    private final String modelData;

    public CustomFloorMapData(int doorX, int doorY, int doorRotation, String modelData, int wallHeight) {
        this.doorX = doorX;
        this.doorY = doorY;
        this.doorRotation = doorRotation;
        this.modelData = modelData;
        this.wallHeight = wallHeight;
    }

    public int getDoorX() {
        return doorX;
    }

    public int getDoorY() {
        return doorY;
    }

    public int getDoorRotation() {
        return doorRotation;
    }

    public String getModelData() {
        return modelData;
    }

    public int getWallHeight() {
        return wallHeight;
    }
}
