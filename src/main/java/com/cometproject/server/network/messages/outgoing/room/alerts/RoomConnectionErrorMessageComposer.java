package com.cometproject.server.network.messages.outgoing.room.alerts;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class RoomConnectionErrorMessageComposer {
    public static Composer compose(int errorCode, String extras) {
        Composer msg = new Composer(Composers.RoomConnectionErrorMessageComposer);

        msg.writeInt(errorCode);

        if(!extras.isEmpty())
            msg.writeString(extras);

        return msg;
    }
}
