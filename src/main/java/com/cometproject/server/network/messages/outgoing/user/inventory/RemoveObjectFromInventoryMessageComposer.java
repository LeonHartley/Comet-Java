package com.cometproject.server.network.messages.outgoing.user.inventory;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class RemoveObjectFromInventoryMessageComposer {
    public static Composer compose(int itemId, int userId) {
        Composer msg = new Composer(Composers.RemoveInventoryObjectMessageComposer);

        msg.writeString(itemId + "");
        msg.writeInt(userId);

        return msg;
    }
}
