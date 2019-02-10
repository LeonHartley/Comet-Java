package com.cometproject.server.network.ws.messages.room.data;

public class EventData {
    private final int roomId;
    private final String name;
    private final String desc;

    public EventData(int roomId, String name, String desc) {
        this.roomId = roomId;
        this.name = name;
        this.desc = desc;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
