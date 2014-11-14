package com.cometproject.server.network.messages.incoming.catalog;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.catalog.CataPageMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class GetCataPageMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int pageId = msg.readInt();

        if(client.getPlayer().cancelPageOpen) {
            client.getPlayer().cancelPageOpen = false;
            return;
        }

        if (CometManager.getCatalog().pageExists(pageId) && CometManager.getCatalog().getPage(pageId).isEnabled()) {
            client.send(CataPageMessageComposer.compose(CometManager.getCatalog().getPage(pageId)));
        }
    }
}
