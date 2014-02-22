package com.cometproject.network.messages.incoming.room.action;

import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.room.avatar.ActionMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class ApplyActionMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        if(client.getPlayer().getEntity().getRoom() != null) {
            int actionId = msg.readInt();

            if(actionId == 5) {
                client.getPlayer().getEntity().setIdle();
            } else {
                client.getPlayer().getEntity().unIdle();
            }

            client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(ActionMessageComposer.compose(client.getPlayer().getEntity().getVirtualId(), actionId));
        }
    }
}
