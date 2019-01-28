package com.cometproject.server.network.ws.messages.piano;

import com.cometproject.server.network.ws.messages.WsMessage;
import com.cometproject.server.network.ws.messages.WsMessageType;

public class ClosePianoMessage extends WsMessage {
    public ClosePianoMessage() {
        super(WsMessageType.PIANO_CLOSE);
    }
}
