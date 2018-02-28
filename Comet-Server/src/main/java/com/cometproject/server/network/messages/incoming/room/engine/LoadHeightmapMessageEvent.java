package com.cometproject.server.network.messages.incoming.room.engine;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.engine.HeightmapMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RelativeHeightmapMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class LoadHeightmapMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom().getModel() == null) {
            return;
        }

        client.sendQueue(new HeightmapMessageComposer(client.getPlayer().getEntity().getRoom()));
        client.sendQueue(new RelativeHeightmapMessageComposer(client.getPlayer().getEntity().getRoom().getModel()));
        client.flush();
    }
}
