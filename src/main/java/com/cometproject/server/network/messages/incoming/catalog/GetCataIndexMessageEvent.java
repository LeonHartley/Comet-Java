package com.cometproject.server.network.messages.incoming.catalog;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.catalog.CatalogIndexMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class GetCataIndexMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        if (msg.readString().equals("BUILDERS_CLUB")) {
            client.getPlayer().cancelPageOpen = true;
            return;
        }

        client.send(CatalogIndexMessageComposer.compose(client.getPlayer().getData().getRank()));
    }
}
