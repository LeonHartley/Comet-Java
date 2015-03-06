package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class TalkMessageComposer extends MessageComposer {
    private final int playerId;
    private final String message;
    private final int emoticon;
    private final int colour;

    public TalkMessageComposer(final int playerId, final String message, final int emoticion, final int colour) {
        this.playerId = playerId;
        this.message = message;
        this.emoticon = emoticion;
        this.colour = colour;
    }

    @Override
    public short getId() {
        return Composers.ChatMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(playerId);
        msg.writeString(message);
        msg.writeInt(emoticon);
        msg.writeInt(colour);
        msg.writeInt(0);
        msg.writeInt(-1);
    }
}
