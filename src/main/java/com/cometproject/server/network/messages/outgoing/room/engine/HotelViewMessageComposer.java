package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class HotelViewMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.OutOfRoomMessageComposer);

        msg.writeShort(2);

        return msg;
    }
}
