package com.cometproject.server.network.ws.messages;

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
