package com.cometsrv.network.messages.incoming.room.engine;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.room.engine.HeightmapMessageComposer;
import com.cometsrv.network.messages.outgoing.room.engine.RelativeHeightmapMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class LoadHeightmapMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(HeightmapMessageComposer.compose(client.getPlayer().getEntity().getRoom().getModel().getMap()));
        client.send(RelativeHeightmapMessageComposer.compose(client.getPlayer().getEntity().getRoom().getModel().getRelativeMap()));
    }
}
