package com.cometproject.server.logging.entries;

public class RoomVisitLogEntry {
    private int id;
    private int playerId;
    private int roomId;
    private int entryTime;
    private int exitTime;

    public RoomVisitLogEntry(int id, int playerId, int roomId, int time) {
        this.id = id;
        this.playerId = playerId;
        this.roomId = roomId;
        this.entryTime = time;
        this.exitTime = 0;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(int entryTime) {
        this.entryTime = entryTime;
    }

    public int getExitTime() {
        return exitTime;
    }

    public void setExitTime(int exitTime) {
        this.exitTime = exitTime;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
