package com.cometproject.server.network.messages.incoming.catalog.groups;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.group.GroupDataMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class GroupFurnitureCatalogMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        client.send(new GroupDataMessageComposer(client.getPlayer().getGroups(), client.getPlayer().getId()));
    }
}
