package com.cometproject.network.messages.incoming.catalog.groups;

import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.catalog.groups.GroupElementsMessageComposer;
import com.cometproject.network.messages.outgoing.catalog.groups.GroupPartsMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class BuyGroupDialogMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(GroupPartsMessageComposer.compose(client.getPlayer().getRooms()));
        client.send(GroupElementsMessageComposer.compose());
    }
}
