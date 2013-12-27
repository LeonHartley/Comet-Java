package com.cometsrv.network.messages.outgoing.room.engine;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class RelativeHeightmapMessageComposer {
    public static Composer compose(String map) {
        Composer msg = new Composer(Composers.RelativeHeightmapMessageComposer);

        msg.writeString(map);

        return msg;
    }
}
