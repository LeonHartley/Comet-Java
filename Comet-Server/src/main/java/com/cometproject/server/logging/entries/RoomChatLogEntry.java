package com.cometproject.server.logging.entries;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.logging.AbstractLogEntry;
import com.cometproject.server.logging.LogEntryType;
import com.cometproject.server.storage.queries.player.PlayerDao;


public class RoomChatLogEntry extends AbstractLogEntry {
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

    public RoomChatLogEntry(int roomId, int userId, String message, int timestamp) {
        this.roomId = roomId;
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public void compose(IComposer msg) {
        msg.writeInt((int) (Comet.getTime() - this.getTimestamp()) * 1000);

        msg.writeInt(this.getPlayerId());
        msg.writeString(PlayerDao.getUsernameByPlayerId(this.getPlayerId()));
        msg.writeString(this.getString());
        msg.writeBoolean(false);
    }

    @Override
    public LogEntryType getType() {
        return LogEntryType.ROOM_CHATLOG;
    }

    @Override
    public String getString() {
        return this.message;
    }

    @Override
    public int getTimestamp() {
        return this.timestamp;
    }

    @Override
    public int getRoomId() {
        return this.roomId;
    }

    @Override
    public int getPlayerId() {
        return this.userId;
    }
}
