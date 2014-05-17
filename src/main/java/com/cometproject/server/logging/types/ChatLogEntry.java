package com.cometproject.server.logging.types;

import com.cometproject.server.logging.AbstractLogEntry;
import com.cometproject.server.logging.LogType;

public class ChatLogEntry extends AbstractLogEntry {

    private int roomId;
    private int userId;
    private String message;

    public ChatLogEntry(int roomId, int userId, String message) {
        this.roomId = roomId;
        this.userId = userId;
        this.message = message;
    }

    @Override
    public LogType getType() {
        return LogType.CHATLOGS;
    }

    @Override
    public String getString() {
        return this.message;
    }

    @Override
    public int getRoomId() {
        return this.roomId;
    }

    @Override
    public int getUserId() {
        return this.userId;
    }
}
