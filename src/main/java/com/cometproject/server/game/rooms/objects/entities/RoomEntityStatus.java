package com.cometproject.server.game.rooms.objects.entities;

public enum RoomEntityStatus {
    SIT("sit"),
    MOVE("mv"),
    LAY("lay");

    private String statusCode;

    RoomEntityStatus(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusCode() {
        return this.statusCode;
    }
}
