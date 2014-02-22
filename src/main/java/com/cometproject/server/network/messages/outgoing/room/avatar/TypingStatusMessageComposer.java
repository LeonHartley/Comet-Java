package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class TypingStatusMessageComposer {
    public static Composer compose(int userId, int status) {
        Composer msg = new Composer(Composers.TypingStatusMessageComposer);

        msg.writeInt(userId);
        msg.writeInt(status);

        return msg;
    }
}
