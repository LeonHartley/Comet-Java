package com.cometproject.network.messages.incoming.catalog.data;

import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.catalog.data.ShopDataMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class GetShopDataMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(ShopDataMessageComposer.compose(1));
    }
}
