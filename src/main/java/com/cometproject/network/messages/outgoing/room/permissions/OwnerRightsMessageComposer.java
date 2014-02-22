package com.cometproject.network.messages.outgoing.room.permissions;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class OwnerRightsMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.OwnerRightsMessageComposer);

        return msg;
    }
}
