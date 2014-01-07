package com.cometsrv.network.messages.incoming.room.action;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.room.avatar.DanceMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class ApplyDanceMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int danceId = msg.readInt();

        if(client.getPlayer().getEntity().getDanceId() == danceId) {
            return;
        }

        client.getPlayer().getEntity().setDanceId(danceId);
        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(DanceMessageComposer.compose(client.getPlayer().getEntity().getVirtualId(), danceId));
    }
}
