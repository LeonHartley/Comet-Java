package com.cometsrv.network.messages.incoming.help;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.help.InitHelpToolMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class InitHelpToolMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(InitHelpToolMessageComposer.compose());
    }
}
