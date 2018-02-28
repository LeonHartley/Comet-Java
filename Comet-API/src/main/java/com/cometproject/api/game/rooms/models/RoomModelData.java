package com.cometproject.api.game.rooms.models;

public class RoomModelData {
    private final String name, heightmap;
    private final int doorX, doorY, doorRotation;
    private int wallHeight;

    public RoomModelData(String name, String heightmap, int doorX, int doorY, int doorRotation, int wallHeight) {
        this.name = name;
        this.heightmap = heightmap;
        this.doorX = doorX;
        this.doorY = doorY;
        this.doorRotation = doorRotation;
        this.wallHeight = wallHeight;
    }

    public String getName() {
        return name;
    }

    public String getHeightmap() {
        return heightmap;
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

    public int getWallHeight() {
        return wallHeight;
    }

    public void setWallHeight(int wallHeight) {
        this.wallHeight = wallHeight;
    }
}
