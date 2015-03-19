package com.cometproject.server.game.moderation.types.tickets;

public enum HelpTicketState {
    OPEN(1),
    CLOSED(0),
    IN_PROGRESS(2);

    private int tabId;

    HelpTicketState(int tabId) {
        this.tabId = tabId;
    }

    public int getTabId() {
        return this.tabId;
    }
}
