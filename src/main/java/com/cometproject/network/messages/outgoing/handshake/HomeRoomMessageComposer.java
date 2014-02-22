package com.cometproject.network.messages.outgoing.handshake;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class HomeRoomMessageComposer {
    public static Composer compose(int roomId) {
        Composer msg = new Composer(Composers.HomeRoomMessageComposer);

        msg.writeInt(roomId);
        msg.writeInt(roomId);

        return msg;
    }
}
