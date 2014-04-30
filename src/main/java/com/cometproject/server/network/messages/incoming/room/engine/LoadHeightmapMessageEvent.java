package com.cometproject.server.network.messages.incoming.room.engine;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.engine.HeightmapMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RelativeHeightmapMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class LoadHeightmapMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(HeightmapMessageComposer.compose(client.getPlayer().getEntity().getRoom().getModel()));
        client.send(RelativeHeightmapMessageComposer.compose(client.getPlayer().getEntity().getRoom().getModel()));
    }
}
