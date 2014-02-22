package com.cometproject.server.network.messages.outgoing.room.items.wired;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class SaveWiredMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.SaveWiredMessageComposer);

        return msg;
    }
}
