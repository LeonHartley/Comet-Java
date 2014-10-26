package com.cometproject.server.network.messages.incoming.handshake;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.handshake.InitCryptoMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.security.EncryptionManager;
import com.cometproject.server.network.sessions.Session;

public class InitCryptoMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        client.send(InitCryptoMessageComposer.compose(EncryptionManager.get().getPrimeKey(), EncryptionManager.get().getGeneratorKey()));
    }
}
