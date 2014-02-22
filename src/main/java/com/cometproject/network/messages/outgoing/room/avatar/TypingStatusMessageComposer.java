package com.cometproject.network.messages.outgoing.room.avatar;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class TypingStatusMessageComposer {
    public static Composer compose(int userId, int status) {
        Composer msg = new Composer(Composers.TypingStatusMessageComposer);

        msg.writeInt(userId);
        msg.writeInt(status);

        return msg;
    }
}
