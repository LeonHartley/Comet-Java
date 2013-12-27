package com.cometsrv.network.messages.outgoing.room.permissions;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class OwnerRightsMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.OwnerRightsMessageComposer);

        return msg;
    }
}
