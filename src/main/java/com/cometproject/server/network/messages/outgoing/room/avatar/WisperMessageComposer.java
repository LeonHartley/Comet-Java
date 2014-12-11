package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class WisperMessageComposer {
    public static Composer compose(int userId, String message, int bubbleId) {
        Composer msg = new Composer(Composers.WhisperMessageComposer);

        msg.writeInt(userId);
        msg.writeString(message);
        msg.writeInt(0);
        msg.writeInt(bubbleId);
        msg.writeInt(0);
        msg.writeInt(-1);

        return msg;
    }

    public static Composer compose(int userId, String message) {
        return compose(userId, message, 0);
    }
}
