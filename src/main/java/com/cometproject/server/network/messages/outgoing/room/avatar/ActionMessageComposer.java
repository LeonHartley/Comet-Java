package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class ActionMessageComposer {
    public static Composer compose(int userId, int actionId) {
        Composer msg = new Composer(Composers.RoomUserActionMessageComposer);

        msg.writeInt(userId);
        msg.writeInt(actionId);

        return msg;
    }
}