package com.cometproject.server.game.moderation.types.tickets;

public class ChatMessage {
    private int playerId;
    private String message;

    public ChatMessage(int playerId, String message) {
        this.playerId = playerId;
        this.message = message;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
