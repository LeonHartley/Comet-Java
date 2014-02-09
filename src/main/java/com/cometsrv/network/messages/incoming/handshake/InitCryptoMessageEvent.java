package com.cometsrv.network.messages.incoming.handshake;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.handshake.InitCryptoMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class InitCryptoMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        client.send(InitCryptoMessageComposer.compose("12f449917de4f94a8c48dbadd92b6276", false));
    }
}
