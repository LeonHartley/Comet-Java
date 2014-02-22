package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class ApplySignMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        // UnIdle the entity
        client.getPlayer().getEntity().unIdle();

        // Add the sign status
        client.getPlayer().getEntity().addStatus("sign", String.valueOf(msg.readInt()));

        // Set the entity to displaying a sign
        client.getPlayer().getEntity().markDisplayingSign();
        client.getPlayer().getEntity().markNeedsUpdate();
    }
}
