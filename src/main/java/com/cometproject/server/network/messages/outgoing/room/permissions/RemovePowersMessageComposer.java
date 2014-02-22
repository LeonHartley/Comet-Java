package com.cometproject.server.network.messages.outgoing.room.permissions;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class RemovePowersMessageComposer {
    public static Composer compose(int userId, int roomId) {
        Composer msg = new Composer(Composers.RemovePowersMessageComposer);

        msg.writeInt(roomId);
        msg.writeInt(userId);

        return msg;
    }
}
