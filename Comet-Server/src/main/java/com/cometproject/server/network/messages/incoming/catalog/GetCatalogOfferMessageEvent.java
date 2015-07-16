package com.cometproject.server.network.messages.incoming.catalog;

import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.catalog.CatalogOfferMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class GetCatalogOfferMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int offerId = msg.readInt();

        if (offerId == -1)
            return;

        CatalogItem catalogItem = CatalogManager.getInstance().getCatalogItemByOfferId(offerId);

        if (catalogItem != null) {
            client.send(new CatalogOfferMessageComposer(catalogItem));
        }
    }
}
