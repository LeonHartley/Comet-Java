package com.cometproject.api.game.rooms.filter;

public interface IFilterResult {
    boolean isBlocked();

    String getMessage();

    boolean wasModified();
}
