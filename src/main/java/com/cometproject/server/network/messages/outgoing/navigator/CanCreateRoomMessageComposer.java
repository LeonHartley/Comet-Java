package com.cometproject.server.network.messages.outgoing.navigator;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class CanCreateRoomMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.CanCreateRoomMessageComposer);

        msg.writeInt(0); // has 25 or more rooms or not
        msg.writeInt(25);

        return msg;
    }
}
