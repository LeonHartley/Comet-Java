package com.cometproject.server.network.messages.outgoing.catalog;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class CatalogOfferMessageComposer extends MessageComposer {
    private final CatalogItem catalogItem;

    public CatalogOfferMessageComposer(final CatalogItem catalogItem) {
        this.catalogItem = catalogItem;
    }

    @Override
    public short getId() {
        return Composers.CatalogOfferMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        this.catalogItem.compose(msg);
    }
}
