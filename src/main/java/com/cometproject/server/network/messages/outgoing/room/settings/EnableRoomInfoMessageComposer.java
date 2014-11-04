package com.cometproject.server.network.messages.outgoing.room.settings;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class EnableRoomInfoMessageComposer {
    public static Composer compose(int roomId) {
        Composer msg = new Composer(Composers.EnableRoomInfoMessageComposer);

        msg.writeInt(roomId);

        return msg;
    }
}
