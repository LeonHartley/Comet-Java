package com.cometsrv.network.messages.incoming.catalog;

import com.cometsrv.game.GameEngine;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.catalog.CataPageMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class GetCataPageMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int pageId = msg.readInt();

        if(GameEngine.getCatalog().pageExists(pageId) && GameEngine.getCatalog().getPage(pageId).isEnabled()) {
            client.send(CataPageMessageComposer.compose(GameEngine.getCatalog().getPage(pageId)));
        }
    }
}
