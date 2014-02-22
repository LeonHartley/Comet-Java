package com.cometproject.network.messages.incoming.room.action;

import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.room.avatar.DanceMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

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
