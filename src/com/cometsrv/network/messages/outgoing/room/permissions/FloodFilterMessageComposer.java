package com.cometsrv.network.messages.outgoing.room.permissions;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class FloodFilterMessageComposer {
    public static Composer compose(double seconds) {
        Composer msg = new Composer(Composers.FloodFilterMessageComposer);

        msg.writeInt((int) Math.round(seconds));

        return msg;
    }
}
