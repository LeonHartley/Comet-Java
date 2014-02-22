package com.cometproject.network.messages.incoming.room.action;

import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.room.avatar.TypingStatusMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class StopTypingMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.getPlayer().getEntity().unIdle();
        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(TypingStatusMessageComposer.compose(client.getPlayer().getEntity().getVirtualId(), 0));
    }
}
