package com.cometproject.server.network.ws.messages;

public class AuthOKMessage extends WsMessage {
    public AuthOKMessage() {
        super(WsMessageType.AUTH_OK);
    }
}
