package com.cometproject.server.network.messages.incoming.catalog;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.catalog.CataPageMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class GetCataPageMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int pageId = msg.readInt();

        if(GameEngine.getCatalog().pageExists(pageId) && GameEngine.getCatalog().getPage(pageId).isEnabled()) {
            client.send(CataPageMessageComposer.compose(GameEngine.getCatalog().getPage(pageId)));
        }
    }
}
