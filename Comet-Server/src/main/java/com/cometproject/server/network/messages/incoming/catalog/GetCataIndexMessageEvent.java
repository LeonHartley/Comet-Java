package com.cometproject.server.network.messages.incoming.catalog;

import com.cometproject.server.composers.catalog.CatalogIndexMessageComposer;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class GetCataIndexMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
//        if (msg.readString().equals("BUILDERS_CLUB")) {
//            client.getPlayer().cancelPageOpen = true;
//            return;
//        }

        // TODO: Make composers totally dumb!!!! they should only take final data, no services
        client.send(new CatalogIndexMessageComposer(CatalogManager.getInstance(), ItemManager.getInstance(), client.getPlayer().getData().getRank()));
    }
}
