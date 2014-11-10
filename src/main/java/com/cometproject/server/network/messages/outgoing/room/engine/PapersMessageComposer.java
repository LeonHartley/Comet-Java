package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class PapersMessageComposer {
    public static Composer compose(String key, String value) {
        Composer msg = new Composer(Composers.RoomSpacesMessageComposer);

        msg.writeString(key);
        msg.writeString(value);

        return msg;
    }
}
