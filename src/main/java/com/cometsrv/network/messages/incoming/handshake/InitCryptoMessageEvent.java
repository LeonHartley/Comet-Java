package com.cometsrv.network.messages.incoming.handshake;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.handshake.InitCryptoMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.security.DiffieHellman;
import com.cometsrv.network.sessions.Session;

import java.math.BigInteger;

public class InitCryptoMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        String ass = new BigInteger(DiffieHellman.generateRandomHex(15), 16).toString();
        client.send(InitCryptoMessageComposer.compose(ass, false));
    }
}
