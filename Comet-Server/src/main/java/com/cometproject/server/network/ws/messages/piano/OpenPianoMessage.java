package com.cometproject.server.network.ws.messages.piano;

import com.cometproject.server.network.ws.messages.WsMessage;
import com.cometproject.server.network.ws.messages.WsMessageType;

public class OpenPianoMessage extends WsMessage {
    public OpenPianoMessage() {
        super(WsMessageType.PIANO_OPEN);
    }
}
