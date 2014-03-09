package com.cometproject.server.network.messages.incoming.handshake;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.handshake.InitCryptoMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.security.DiffieHellman;
import com.cometproject.server.network.sessions.Session;

import java.math.BigInteger;

public class InitCryptoMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        //client.send(InitCryptoMessageComposer.compose("12f449917de4f94a8c48dbadd92b6276", false));
        client.send(InitCryptoMessageComposer.compose(new BigInteger(DiffieHellman.GenerateRandomHexString(15), 16).toString(16), false));
        //client.send(InitCryptoMessageComposer.compose("00a2acf44a945c81b56c71f1b6df8acb", false));
    }
}
