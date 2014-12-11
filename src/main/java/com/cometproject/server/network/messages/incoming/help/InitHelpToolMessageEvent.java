package com.cometproject.server.network.messages.incoming.help;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.help.InitHelpToolMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class InitHelpToolMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(InitHelpToolMessageComposer.compose());
    }
}
