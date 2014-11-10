package com.cometproject.server.game.rooms.models;

public class CustomFloorMapData {
    private int doorX, doorY, doorRotation, wallHeight;
    private String modelData;

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

    public void setDoorX(int doorX) {
        this.doorX = doorX;
    }

    public int getDoorY() {
        return doorY;
    }

    public void setDoorY(int doorY) {
        this.doorY = doorY;
    }

    public int getDoorRotation() {
        return doorRotation;
    }

    public void setDoorRotation(int doorRotation) {
        this.doorRotation = doorRotation;
    }

    public String getModelData() {
        return modelData;
    }

    public void setModelData(String modelData) {
        this.modelData = modelData;
    }

    public int getWallHeight() {
        return wallHeight;
    }

    public void setWallHeight(int wallHeight) {
        this.wallHeight = wallHeight;
    }
}
