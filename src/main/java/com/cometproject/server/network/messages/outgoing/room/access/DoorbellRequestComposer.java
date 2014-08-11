package com.cometproject.server.network.messages.outgoing.room.access;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class DoorbellRequestComposer {
    public static Composer compose(String username) {
        Composer msg = new Composer(Composers.DoorBellRequestComposer);
        msg.writeString(username);

        return msg;
    }
}
