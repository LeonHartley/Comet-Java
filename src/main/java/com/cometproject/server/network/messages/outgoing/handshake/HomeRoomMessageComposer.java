package com.cometproject.server.network.messages.outgoing.handshake;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class HomeRoomMessageComposer {
    public static Composer compose(int roomId) {
        Composer msg = new Composer(Composers.HomeRoomMessageComposer);

        msg.writeInt(roomId);
        msg.writeInt(roomId);

        return msg;
    }
}
