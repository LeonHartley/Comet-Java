package com.cometproject.network.messages.outgoing.room.avatar;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class ActionMessageComposer {
    public static Composer compose(int userId, int actionId) {
        Composer msg = new Composer(Composers.ActionMessageComposer);

        msg.writeInt(userId);
        msg.writeInt(actionId);

        return msg;
    }
}