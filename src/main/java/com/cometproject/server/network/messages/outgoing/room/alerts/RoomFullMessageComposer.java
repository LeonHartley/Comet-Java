package com.cometproject.server.network.messages.outgoing.room.alerts;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class RoomFullMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.RoomFullMessageComposer);

        msg.writeInt(1);
        msg.writeString("/x363");

        return msg;
    }
}
