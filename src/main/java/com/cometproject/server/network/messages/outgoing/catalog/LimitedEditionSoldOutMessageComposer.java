package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class LimitedEditionSoldOutMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.CatalogLimitedItemSoldOutMessageComposer);

        return msg;
    }
}
