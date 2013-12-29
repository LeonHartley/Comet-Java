package com.cometsrv.network.messages.outgoing.room.avatar;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class GiveRespectMessageComposer {
    public static Composer compose(int userId, int totalRespects) {
        Composer msg = new Composer(Composers.GiveRespectMessageComposer);

        msg.writeInt(userId);
        msg.writeInt(totalRespects);

        return msg;
    }
}
