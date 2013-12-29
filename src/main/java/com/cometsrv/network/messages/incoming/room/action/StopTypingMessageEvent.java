package com.cometsrv.network.messages.incoming.room.action;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.room.avatar.TypingStatusMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class StopTypingMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.getPlayer().getAvatar().unidle();
        client.getPlayer().getAvatar().getRoom().getAvatars().broadcast(TypingStatusMessageComposer.compose(client.getPlayer().getId(), 0));
    }
}
