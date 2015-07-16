package com.cometproject.server.network.messages.incoming.catalog.groups;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.catalog.groups.GroupElementsMessageComposer;
import com.cometproject.server.network.messages.outgoing.catalog.groups.GroupPartsMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class BuyGroupDialogMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        client.send(new GroupPartsMessageComposer(client.getPlayer().getRooms()));
        client.send(new GroupElementsMessageComposer());
    }
}
