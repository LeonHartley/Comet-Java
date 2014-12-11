package com.cometproject.server.network.messages.outgoing.room.permissions;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class OwnerRightsMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.HasOwnerRightsMessageComposer);

        return msg;
    }
}
