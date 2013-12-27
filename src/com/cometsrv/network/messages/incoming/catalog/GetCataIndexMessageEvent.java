package com.cometsrv.network.messages.incoming.catalog;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.catalog.CataIndexMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class GetCataIndexMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(CataIndexMessageComposer.compose(client.getPlayer().getData().getRank()));
    }
}
