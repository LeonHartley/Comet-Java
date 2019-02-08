package com.cometproject.server.network.ws.messages.alerts;

import com.cometproject.server.network.ws.messages.WsMessage;
import com.cometproject.server.network.ws.messages.WsMessageType;

public class NewBadgeMessage extends WsMessage {
    private final String badge;

    public NewBadgeMessage(final String badge) {
        super(WsMessageType.NEW_BADGE);

        this.badge = badge;
    }

    public String getBadge() {
        return badge;
    }
}
