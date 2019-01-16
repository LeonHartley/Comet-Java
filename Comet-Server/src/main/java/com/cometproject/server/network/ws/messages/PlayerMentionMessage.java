package com.cometproject.server.network.ws.messages;

public class PlayerMentionMessage extends WsMessage {
    private final String user;
    private final int roomId;

    public PlayerMentionMessage(String user, int roomId) {
        super(WsMessageType.PLAYER_MENTION);
        this.user = user;
        this.roomId = roomId;
    }

    public String getUser() {
        return user;
    }

    public int getRoomId() {
        return roomId;
    }
}
