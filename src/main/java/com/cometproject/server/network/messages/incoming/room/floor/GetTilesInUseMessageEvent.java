package com.cometproject.server.network.messages.incoming.room.floor;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.floor.TilesInUseMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class GetTilesInUseMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        if (client.getPlayer().getEntity() != null)
            client.send(TilesInUseMessageComposer.compose(client.getPlayer().getEntity().getRoom().getMapping().tilesWithFurniture()));
    }
}
