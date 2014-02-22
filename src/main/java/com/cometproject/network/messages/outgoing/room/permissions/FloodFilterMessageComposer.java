package com.cometproject.network.messages.outgoing.room.permissions;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class FloodFilterMessageComposer {
    public static Composer compose(double seconds) {
        Composer msg = new Composer(Composers.FloodFilterMessageComposer);

        msg.writeInt((int) Math.round(seconds));

        return msg;
    }
}
