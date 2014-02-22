package com.cometproject.network.messages.outgoing.room.engine;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class HotelViewMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.HotelViewMessageComposer);

        msg.writeString("");

        return msg;
    }
}
