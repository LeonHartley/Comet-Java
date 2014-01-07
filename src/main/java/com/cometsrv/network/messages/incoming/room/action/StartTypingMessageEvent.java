package com.cometsrv.network.messages.incoming.room.action;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.room.avatar.TypingStatusMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class StartTypingMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.getPlayer().getEntity().unIdle();
        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(TypingStatusMessageComposer.compose(client.getPlayer().getEntity().getVirtualId(), 1));
    }
}
