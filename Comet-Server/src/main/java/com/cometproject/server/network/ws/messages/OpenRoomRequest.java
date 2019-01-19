package com.cometproject.server.network.ws.messages;

public class OpenRoomRequest {
    private final int roomId;

    public OpenRoomRequest(int roomId) {
        this.roomId = roomId;
    }

    public int getRoomId() {
        return roomId;
    }
}