package com.cometproject.server.network.ws.messages.piano;

import com.cometproject.server.network.ws.messages.WsMessage;
import com.cometproject.server.network.ws.messages.WsMessageType;

public class PlayPianoMessage extends WsMessage {
    private final String note;

    public PlayPianoMessage(String note) {
        super(WsMessageType.PIANO_PLAY_NOTE);

        this.note = note;
    }

    public String getNote() {
        return note;
    }
}
