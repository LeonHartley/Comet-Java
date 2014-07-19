package com.cometproject.server.game.rooms.filter;

public class FilterResult {
    private boolean isBlocked;
    private boolean wasModified;
    private String chatMessage;

    public FilterResult(String chatMessage) {
        this.isBlocked = false;
        this.wasModified = false;
        this.chatMessage = chatMessage;
    }

    public FilterResult(boolean isBlocked, String chatMessage) {
        this.isBlocked = isBlocked;
        this.wasModified = false;
        this.chatMessage = chatMessage;
    }

    public FilterResult(String chatMessage, boolean wasModified) {
        this.isBlocked = false;
        this.wasModified = wasModified;
        this.chatMessage = chatMessage;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public boolean wasModified() {
        return wasModified;
    }
}
