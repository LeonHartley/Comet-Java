package com.cometproject.server.composers.catalog;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.api.game.catalog.types.ICatalogItem;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class CatalogOfferMessageComposer extends MessageComposer {
    private final ICatalogItem catalogItem;

    public CatalogOfferMessageComposer(final ICatalogItem catalogItem) {
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
