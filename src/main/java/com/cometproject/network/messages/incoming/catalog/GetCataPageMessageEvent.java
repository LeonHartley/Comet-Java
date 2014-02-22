package com.cometproject.network.messages.incoming.catalog;

import com.cometproject.game.GameEngine;
import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.catalog.CataPageMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class GetCataPageMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int pageId = msg.readInt();

        if(GameEngine.getCatalog().pageExists(pageId) && GameEngine.getCatalog().getPage(pageId).isEnabled()) {
            client.send(CataPageMessageComposer.compose(GameEngine.getCatalog().getPage(pageId)));
        }
    }
}
