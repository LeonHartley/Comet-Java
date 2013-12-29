package com.cometsrv.network.messages.outgoing.navigator;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class CanCreateRoomMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.CanCreateRoomMessageComposer);

        msg.writeInt(0); // has 25 or more rooms or not
        msg.writeInt(25);

        return msg;
    }
}
