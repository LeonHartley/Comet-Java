package com.cometsrv.network.messages.incoming.catalog.data;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.catalog.data.ShopDataMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class GetShopDataMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(ShopDataMessageComposer.compose(1));
    }
}
