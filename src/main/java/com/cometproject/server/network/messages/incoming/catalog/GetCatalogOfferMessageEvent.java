package com.cometproject.server.network.messages.incoming.catalog;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.catalog.CatalogOfferMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class GetCatalogOfferMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int offerId = msg.readInt();

        if (offerId == -1)
            return;

        CatalogItem catalogItem = CometManager.getCatalog().getCatalogItemByOfferId(offerId);

        if (catalogItem != null) {
            client.send(CatalogOfferMessageComposer.compose(catalogItem));
        }
    }
}
