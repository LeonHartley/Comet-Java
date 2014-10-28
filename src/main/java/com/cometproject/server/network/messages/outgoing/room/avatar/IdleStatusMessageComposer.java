package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class IdleStatusMessageComposer {
    public static Composer compose(int userId, boolean isIdle) {
        Composer msg = new Composer(Composers.RoomUserIdleMessageComposer);

        msg.writeInt(userId);
        msg.writeBoolean(isIdle);

        return msg;
    }
}
