package com.cometproject.server.network.messages.outgoing.room.permissions;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class FloodFilterMessageComposer {
    public static Composer compose(double seconds) {
        Composer msg = new Composer(Composers.FloodFilterMessageComposer);

        msg.writeInt((int) Math.round(seconds));

        return msg;
    }
}
