package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.avatar.ActionMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class ApplyActionMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        if (client.getPlayer() != null && client.getPlayer().getEntity() != null && client.getPlayer().getEntity().getRoom() != null) {
            int actionId = msg.readInt();

            if (actionId == 5) {
                client.getPlayer().getEntity().setIdle();
            } else {
                client.getPlayer().getEntity().unIdle();
            }

            client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new ActionMessageComposer(client.getPlayer().getEntity().getId(), actionId));
        }
    }
}
