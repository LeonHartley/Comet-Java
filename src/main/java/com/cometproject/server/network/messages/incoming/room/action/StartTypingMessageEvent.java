package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.avatar.TypingStatusMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class StartTypingMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        if (client.getPlayer() == null || client.getPlayer().getEntity() == null)
            return;

        client.getPlayer().getEntity().unIdle();
        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(TypingStatusMessageComposer.compose(client.getPlayer().getEntity().getVirtualId(), 1));
    }
}
