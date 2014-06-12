package com.cometproject.server.logging.entries;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.logging.AbstractLogEntry;
import com.cometproject.server.logging.LogEntryType;
import com.cometproject.server.logging.old.LogType;

public class RoomChatLogEntry {
    private int roomId;
    private int userId;
    private String message;
    private int timestamp;

    public RoomChatLogEntry(int roomId, int userId, String message) {
        this.roomId = roomId;
        this.userId = userId;
        this.message = message;
        this.timestamp = (int) Comet.getTime();
    }

    public LogEntryType getType() {
        return LogEntryType.ROOM_CHATLOG;
    }

    public String getString() {
        return this.message;
    }

    public int getTimestamp() {
        return this.timestamp;
    }

    public int getRoomId() {
        return this.roomId;
    }

    public int getUserId() {
        return this.userId;
    }}
