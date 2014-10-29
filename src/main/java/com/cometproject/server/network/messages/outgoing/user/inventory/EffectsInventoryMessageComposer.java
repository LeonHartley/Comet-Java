package com.cometproject.server.network.messages.outgoing.user.inventory;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class EffectsInventoryMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.EffectsInventoryMessageComposer);

        msg.writeInt(0);

        return msg;
    }
}
