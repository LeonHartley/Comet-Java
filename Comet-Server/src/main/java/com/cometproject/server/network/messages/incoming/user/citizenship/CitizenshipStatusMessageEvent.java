package com.cometproject.server.network.messages.incoming.user.citizenship;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.user.permissions.CitizenshipStatusMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class CitizenshipStatusMessageEvent implements Event {

    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
//        client.send(new CitizenshipStatusMessageComposer());
    }
}
