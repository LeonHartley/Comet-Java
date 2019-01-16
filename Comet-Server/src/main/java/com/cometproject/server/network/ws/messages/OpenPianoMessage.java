package com.cometproject.server.network.ws.messages;

public class OpenPianoMessage extends WsMessage {
    public OpenPianoMessage() {
        super(WsMessageType.PIANO_OPEN);
    }
}
