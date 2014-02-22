package com.cometproject.server.network.messages.outgoing.room.items.wired;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class WiredTriggerMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.WiredTriggerMessageComposer);
        //TODO this
        return msg;
    }
}
