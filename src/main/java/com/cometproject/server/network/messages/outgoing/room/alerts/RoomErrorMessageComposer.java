package com.cometproject.server.network.messages.outgoing.room.alerts;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class RoomErrorMessageComposer {
    public static Composer compose(int errorCode) {
        Composer msg = new Composer(Composers.RoomErrorMessageComposer);
        msg.writeInt(errorCode);

        return msg;
    }
}
