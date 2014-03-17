package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class DropHandItemMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        client.getPlayer().getEntity().carryItem(0);
    }
}
