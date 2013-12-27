package com.cometsrv.network.messages.incoming.catalog.groups;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.catalog.groups.GroupElementsMessageComposer;
import com.cometsrv.network.messages.outgoing.catalog.groups.GroupPartsMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class BuyGroupDialogMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(GroupPartsMessageComposer.compose(client.getPlayer().getRooms()));
        client.send(GroupElementsMessageComposer.compose());
    }
}
