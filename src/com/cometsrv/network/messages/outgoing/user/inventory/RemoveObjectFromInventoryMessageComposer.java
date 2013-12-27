package com.cometsrv.network.messages.outgoing.user.inventory;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class RemoveObjectFromInventoryMessageComposer {
    public static Composer compose(int itemId) {
        Composer msg = new Composer(Composers.RemoveObjectFromInventoryMessageComposer);

        msg.writeInt(itemId);

        return msg;
    }
}
