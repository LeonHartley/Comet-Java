package com.cometproject.server.network.messages.outgoing.room.items.lovelock;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class LoveLockConfirmedMessageComposer {
    public static Composer compose(int itemId) {
        Composer composer = new Composer(Composers.LoveLockConfirmedMessageComposer);

        composer.writeInt(itemId);

        return composer;
    }
}
