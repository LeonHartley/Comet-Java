package com.cometproject.network.messages.outgoing.room.avatar;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class IdleStatusMessageComposer {
    public static Composer compose(int userId, boolean isIdle) {
        Composer msg = new Composer(Composers.IdleStatusMessageComposer);

        msg.writeInt(userId);
        msg.writeBoolean(isIdle);

        return msg;
    }
}
