package com.cometsrv.network.messages.outgoing.room.permissions;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class RemovePowersMessageComposer {
    public static Composer compose(int userId, int roomId) {
        Composer msg = new Composer(Composers.RemovePowersMessageComposer);

        msg.writeInt(roomId);
        msg.writeInt(userId);

        return msg;
    }
}
