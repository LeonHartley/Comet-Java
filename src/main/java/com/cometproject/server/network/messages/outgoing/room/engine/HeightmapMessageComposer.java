package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class HeightmapMessageComposer {
    public static Composer compose(String map) {
        Composer msg = new Composer(Composers.HeightmapMessageComposer);

        msg.writeString(map);

        return msg;
    }
}
