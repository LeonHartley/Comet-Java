package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class CatalogOfferMessageComposer {
    public static Composer compose(CatalogItem catalogItem) {
        Composer msg = new Composer(Composers.CatalogOfferMessageComposer);

        catalogItem.compose(msg);

        return msg;
    }
}
