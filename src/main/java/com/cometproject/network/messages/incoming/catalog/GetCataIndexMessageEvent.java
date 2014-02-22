package com.cometproject.network.messages.incoming.catalog;

import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.catalog.CataIndexMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class GetCataIndexMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(CataIndexMessageComposer.compose(client.getPlayer().getData().getRank()));
    }
}
