package com.cometproject.server.network.messages.incoming.room.engine;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class LoadHeightmapMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(client.getPlayer().getEntity().getRoom().getModel().getHeightmapMessage());
        client.send(client.getPlayer().getEntity().getRoom().getModel().getRelativeHeightmapMessage());
    }
}
