package com.cometproject.network.messages.outgoing.user.inventory;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class UpdateInventoryMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.UpdateInventoryMessageComposer);

        return msg;
    }
}
