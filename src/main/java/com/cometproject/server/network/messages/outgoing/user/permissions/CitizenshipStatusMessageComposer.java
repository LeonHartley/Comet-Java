package com.cometproject.server.network.messages.outgoing.user.permissions;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class CitizenshipStatusMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.CitizenshipStatusMessageComposer);

        msg.writeString("citizenship");
        msg.writeInt(4);
        msg.writeInt(4);

        return msg;
    }
}
