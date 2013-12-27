package com.cometsrv.network.messages.outgoing.room.items;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class RemoveWallItemMessageComposer {
    public static Composer compose(int itemId, int userId) {
        Composer msg = new Composer(Composers.RemoveWallItemMessageComposer);

        msg.writeString(itemId);
        msg.writeInt(userId);

        return msg;
    }
}
