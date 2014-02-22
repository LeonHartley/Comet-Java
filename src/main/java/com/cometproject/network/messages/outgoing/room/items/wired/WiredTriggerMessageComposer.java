package com.cometproject.network.messages.outgoing.room.items.wired;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class WiredTriggerMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.WiredTriggerMessageComposer);
        //TODO this
        return msg;
    }
}
