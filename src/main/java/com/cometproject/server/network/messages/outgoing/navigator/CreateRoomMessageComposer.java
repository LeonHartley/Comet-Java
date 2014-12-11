package com.cometproject.server.network.messages.outgoing.navigator;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class CreateRoomMessageComposer {
    public static Composer compose(int roomId, String name) {
        Composer msg = new Composer(Composers.OnCreateRoomInfoMessageComposer);

        msg.writeInt(roomId);
        msg.writeString(name);

        return msg;
    }
}
