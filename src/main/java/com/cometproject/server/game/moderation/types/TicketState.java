package com.cometproject.server.game.moderation.types;

public enum TicketState {
    OPEN,
    CLOSED;

    public static TicketState getState(String state) {
        if (state.equals("open"))
            return TicketState.OPEN;
        else
            return TicketState.CLOSED;
    }
}
