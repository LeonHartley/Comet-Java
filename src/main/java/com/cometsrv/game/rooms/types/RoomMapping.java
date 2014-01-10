package com.cometsrv.game.rooms.types;

public class RoomMapping {
    private final RoomModel model;

    public RoomMapping(RoomModel roomModel) {
        this.model = roomModel;
    }

    public boolean isValidRotation(int rotation) {
        return (rotation == 0 || rotation == 2 || rotation == 4 || rotation == 6);
    }
}
