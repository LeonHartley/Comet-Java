package com.cometproject.server.network.messages.incoming.catalog.data;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.catalog.data.CatalogOfferConfigMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class CatalogOfferConfigMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        client.send(new CatalogOfferConfigMessageComposer());
    }
}
