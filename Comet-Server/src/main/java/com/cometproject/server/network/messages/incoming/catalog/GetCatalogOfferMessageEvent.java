package com.cometproject.server.network.messages.incoming.catalog;

import com.cometproject.api.game.catalog.types.ICatalogItem;
import com.cometproject.server.composers.catalog.CatalogOfferMessageComposer;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class GetCatalogOfferMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int offerId = msg.readInt();

        if (offerId == -1)
            return;

        ICatalogItem catalogItem = CatalogManager.getInstance().getCatalogItemByOfferId(offerId);

        if (catalogItem != null) {
            client.send(new CatalogOfferMessageComposer(catalogItem));
        }
    }
}
