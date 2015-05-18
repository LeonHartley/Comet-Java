package com.cometproject.server.logging.entries;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.logging.AbstractLogEntry;
import com.cometproject.server.logging.LogEntryType;

public class MessengerChatLogEntry extends AbstractLogEntry {
    private int senderId;
    private int receiverId;
    private String message;
    private int timestamp;

    public MessengerChatLogEntry(int senderId, int receiverId, String message) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.timestamp = (int) Comet.getTime();
    }

    @Override
    public LogEntryType getType() {
        return LogEntryType.MESSENGER_CHATLOG;
    }

    @Override
    public String getString() {
        return "To: " + this.receiverId + ", Message: " + this.message;
    }

    @Override
    public int getTimestamp() {
        return this.timestamp;
    }

    @Override
    public int getUserId() {
        return this.senderId;
    }
}
