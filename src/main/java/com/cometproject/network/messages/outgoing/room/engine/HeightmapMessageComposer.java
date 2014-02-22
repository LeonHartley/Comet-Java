package com.cometproject.network.messages.outgoing.room.engine;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class HeightmapMessageComposer {
    public static Composer compose(String map) {
        Composer msg = new Composer(Composers.HeightmapMessageComposer);

        msg.writeString(map);

        return msg;
    }
}
