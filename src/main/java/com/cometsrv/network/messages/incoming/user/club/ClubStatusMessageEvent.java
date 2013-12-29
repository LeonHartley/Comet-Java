package com.cometsrv.network.messages.incoming.user.club;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.user.club.ClubStatusMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class ClubStatusMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(ClubStatusMessageComposer.compose(client.getPlayer().getSubscription()));
    }
}
