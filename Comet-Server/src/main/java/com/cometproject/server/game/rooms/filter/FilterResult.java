package com.cometproject.server.game.rooms.filter;

import com.cometproject.api.game.rooms.filter.IFilterResult;

public class FilterResult implements IFilterResult {
    private boolean isBlocked;
    private boolean wasModified;
    private String message;

    public FilterResult(String chatMessage) {
        this.isBlocked = false;
        this.wasModified = false;
        this.message = chatMessage;
    }

    public FilterResult(boolean isBlocked, String chatMessage) {
        this.isBlocked = isBlocked;
        this.wasModified = false;
        this.message = chatMessage;
    }

    public FilterResult(String chatMessage, boolean wasModified) {
        this.isBlocked = false;
        this.wasModified = wasModified;
        this.message = chatMessage;
    }

    @Override
    public boolean isBlocked() {
        return isBlocked;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public boolean wasModified() {
        return wasModified;
    }
}
