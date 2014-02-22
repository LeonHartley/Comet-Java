package com.cometproject.network.messages.outgoing.room.engine;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class RelativeHeightmapMessageComposer {
    public static Composer compose(String map) {
        Composer msg = new Composer(Composers.RelativeHeightmapMessageComposer);

        msg.writeString(map);

        return msg;
    }
}
