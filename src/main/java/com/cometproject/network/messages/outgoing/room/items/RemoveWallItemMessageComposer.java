package com.cometproject.network.messages.outgoing.room.items;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class RemoveWallItemMessageComposer {
    public static Composer compose(int itemId, int userId) {
        Composer msg = new Composer(Composers.RemoveWallItemMessageComposer);

        msg.writeString(itemId);
        msg.writeInt(userId);

        return msg;
    }
}
