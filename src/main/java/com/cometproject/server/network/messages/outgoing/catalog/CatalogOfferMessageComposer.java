package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class CatalogOfferMessageComposer extends MessageComposer {
    private final CatalogItem catalogItem;

    public CatalogOfferMessageComposer(final CatalogItem catalogItem) {
        this.catalogItem = catalogItem;
    }

    @Override
    public short getId() {
        return Composers.CatalogOfferConfigMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        this.catalogItem.compose(msg);
    }
}
