package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class InitializeFriendListMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        client.getPlayer().getMessenger().initialise();
    }
}
