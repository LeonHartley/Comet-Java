package com.cometproject.server.network.messages.outgoing.user.inventory;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class UpdateInventoryMessageComposer {
    public static Composer compose() {
        return new Composer(Composers.UpdateInventoryMessageComposer);
    }
}
