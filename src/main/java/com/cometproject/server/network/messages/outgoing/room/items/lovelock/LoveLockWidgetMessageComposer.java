package com.cometproject.server.network.messages.outgoing.room.items.lovelock;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class LoveLockWidgetMessageComposer {
    public static Composer compose(int itemId) {
        Composer composer = new Composer(Composers.LoveLockWidgetMessageComposer);

        composer.writeInt(itemId);
        composer.writeBoolean(true);

        return composer;
    }
}
