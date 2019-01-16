package com.cometproject.server.network.ws.messages;

public abstract class WsMessage {
    private final WsMessageType type;

    WsMessage(WsMessageType type) {
        this.type = type;
    }

    public WsMessageType getType() {
        return type;
    }
}
