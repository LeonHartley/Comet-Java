package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class GiftUserNotFoundMessageComposer {
    public static Composer compose() {
        return new Composer(Composers.GiftUserNotFoundMessageComposer);
    }
}
