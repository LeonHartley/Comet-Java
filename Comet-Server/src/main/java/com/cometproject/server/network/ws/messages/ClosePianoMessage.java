package com.cometproject.server.network.ws.messages;

public class ClosePianoMessage extends WsMessage {
    public ClosePianoMessage() {
        super(WsMessageType.PIANO_CLOSE);
    }
}
