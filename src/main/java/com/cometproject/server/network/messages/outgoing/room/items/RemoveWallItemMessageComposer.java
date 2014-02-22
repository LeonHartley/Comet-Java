package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class RemoveWallItemMessageComposer {
    public static Composer compose(int itemId, int userId) {
        Composer msg = new Composer(Composers.RemoveWallItemMessageComposer);

        msg.writeString(itemId);
        msg.writeInt(userId);

        return msg;
    }
}
