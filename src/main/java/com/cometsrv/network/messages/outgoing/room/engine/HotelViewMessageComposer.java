package com.cometsrv.network.messages.outgoing.room.engine;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class HotelViewMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.HotelViewMessageComposer);

        msg.writeString("");

        return msg;
    }
}
