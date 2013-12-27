package com.cometsrv.network.messages.outgoing.room.engine;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class HeightmapMessageComposer {
    public static Composer compose(String map) {
        Composer msg = new Composer(Composers.HeightmapMessageComposer);

        msg.writeString(map);

        return msg;
    }
}
