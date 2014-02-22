package com.cometproject.game.rooms.types.components.types;

public class ChatMessage {
    private int userId;
    private String message;
    private long time;

    public ChatMessage(int userId, String message, long time) {
        this.userId = userId;
        this.message = message;
        this.time = time;
    }

    public int getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public long getTime() {
        return time;
    }
}
