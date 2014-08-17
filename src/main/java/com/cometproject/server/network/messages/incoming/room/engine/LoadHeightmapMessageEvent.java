package com.cometproject.server.network.messages.incoming.room.engine;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class LoadHeightmapMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom().getModel() == null) {
            return;
        }

        client.sendQueue(client.getPlayer().getEntity().getRoom().getModel().getHeightmapMessage());
        client.sendQueue(client.getPlayer().getEntity().getRoom().getModel().getRelativeHeightmapMessage());
        client.flush();
    }
}
