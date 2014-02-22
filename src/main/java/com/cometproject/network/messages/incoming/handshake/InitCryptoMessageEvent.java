package com.cometproject.network.messages.incoming.handshake;

import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.handshake.InitCryptoMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class InitCryptoMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        client.send(InitCryptoMessageComposer.compose("12f449917de4f94a8c48dbadd92b6276", false));
    }
}
