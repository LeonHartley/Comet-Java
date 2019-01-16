package com.cometproject.server.network.ws.request;

public class WsRequest {
    private final WsRequestType type;

    public WsRequest(WsRequestType type) {
        this.type = type;
    }

    public WsRequestType getType() {
        return type;
    }
}
