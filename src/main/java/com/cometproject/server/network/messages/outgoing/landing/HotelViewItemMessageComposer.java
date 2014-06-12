package com.cometproject.server.network.messages.outgoing.landing;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class HotelViewItemMessageComposer {
    public static Composer compose(String key, String value) {
        Composer msg = new Composer(Composers.HotelViewItemMessageComposer);

        msg.writeString(key);
        msg.writeString(value);

        return msg;
    }
}
