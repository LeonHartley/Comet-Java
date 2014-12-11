package com.cometproject.server.network.messages.incoming.user.citizenship;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.user.permissions.CitizenshipStatusMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class CitizenshipStatusMessageEvent implements IEvent {

    @Override
    public void handle(Session client, Event msg) throws Exception {
        client.send(CitizenshipStatusMessageComposer.compose());
    }
}
