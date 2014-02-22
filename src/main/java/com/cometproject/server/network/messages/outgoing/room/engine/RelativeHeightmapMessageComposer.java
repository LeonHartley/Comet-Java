package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class RelativeHeightmapMessageComposer {
    public static Composer compose(String map) {
        Composer msg = new Composer(Composers.RelativeHeightmapMessageComposer);

        msg.writeString(map);

        return msg;
    }
}
