package com.cometproject.server.network.messages.incoming.catalog.groups;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.group.GroupDataMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class GroupFurnitureCatalogMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        client.send(new GroupDataMessageComposer(client.getPlayer().getGroups(), client.getPlayer().getId()));
    }
}
