package com.cometproject.server.network.messages.outgoing.user.inventory;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class RemoveObjectFromInventoryMessageComposer {
    public static Composer compose(int itemId) {
        Composer msg = new Composer(Composers.RemoveObjectFromInventoryMessageComposer);

        msg.writeInt(itemId);

        return msg;
    }
}
