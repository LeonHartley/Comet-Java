package com.cometsrv.network.messages.outgoing.handshake;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class HomeRoomMessageComposer {
    public static Composer compose(int roomId) {
        Composer msg = new Composer(Composers.HomeRoomMessageComposer);

        msg.writeInt(roomId);
        msg.writeInt(roomId);

        return msg;
    }
}
