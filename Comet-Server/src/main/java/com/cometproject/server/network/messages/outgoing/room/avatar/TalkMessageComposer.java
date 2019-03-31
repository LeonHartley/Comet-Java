package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.types.misc.ChatEmotion;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class TalkMessageComposer extends MessageComposer {
    private final int entityId;
    private final String message;
    private final ChatEmotion emoticon;
    private final int colour;

    public TalkMessageComposer(final int entityId, final String message, final ChatEmotion emoticion, final int colour) {
        this.entityId = entityId;
        this.message = message;
        this.emoticon = emoticion;
        this.colour = colour;
    }

    @Override
    public short getId() {
        return Composers.ChatMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(entityId);
        msg.writeString(message);
        msg.writeInt(emoticon.getEmotionId());
        msg.writeInt(colour);
        msg.writeInt(0);
        msg.writeInt(-1);
    }
}
