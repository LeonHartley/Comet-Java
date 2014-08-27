package com.cometproject.server.network.messages.incoming.user.club;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.user.club.ClubStatusMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class ClubStatusMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(ClubStatusMessageComposer.compose(client.getPlayer().getSubscription()));
        client.send(client.getPlayer().composeCurrenciesBalance());
    }
}
