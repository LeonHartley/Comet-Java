package com.cometsrv.network.messages.outgoing.user.inventory;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class UpdateInventoryMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.UpdateInventoryMessageComposer);

        return msg;
    }
}
