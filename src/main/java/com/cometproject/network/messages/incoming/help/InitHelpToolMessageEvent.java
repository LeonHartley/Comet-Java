package com.cometproject.network.messages.incoming.help;

import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.help.InitHelpToolMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class InitHelpToolMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(InitHelpToolMessageComposer.compose());
    }
}
