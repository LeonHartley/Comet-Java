package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class TalkMessageComposer {
    public static Composer compose(int userId, String message, int emoticon, int colour) {
        Composer msg = new Composer(Composers.ChatMessageComposer);

        msg.writeInt(userId);
        msg.writeString(message);
        msg.writeInt(emoticon);
        msg.writeInt(colour);
        msg.writeInt(0);
        msg.writeInt(-1);

        return msg;
    }
}
