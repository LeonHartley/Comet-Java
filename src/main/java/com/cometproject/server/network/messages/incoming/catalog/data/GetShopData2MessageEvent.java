package com.cometproject.server.network.messages.incoming.catalog.data;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.catalog.data.OfferMessageComposer;
import com.cometproject.server.network.messages.outgoing.catalog.data.ShopDataMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class GetShopData2MessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(ShopDataMessageComposer.compose(2));
        client.send(OfferMessageComposer.compose());
    }
}
