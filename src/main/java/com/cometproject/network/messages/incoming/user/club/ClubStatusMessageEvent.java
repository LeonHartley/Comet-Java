package com.cometproject.network.messages.incoming.user.club;

import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.user.club.ClubStatusMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class ClubStatusMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(ClubStatusMessageComposer.compose(client.getPlayer().getSubscription()));
    }
}
