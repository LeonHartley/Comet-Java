package com.cometproject.server.network.messages.incoming.catalog;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.catalog.CatalogIndexMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class GetCataIndexMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
//        if (msg.readString().equals("BUILDERS_CLUB")) {
//            client.getPlayer().cancelPageOpen = true;
//            return;
//        }

        client.send(new CatalogIndexMessageComposer(client.getPlayer().getData().getRank()));
    }
}
