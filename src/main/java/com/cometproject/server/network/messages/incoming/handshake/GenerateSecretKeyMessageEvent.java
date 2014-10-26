package com.cometproject.server.network.messages.incoming.handshake;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.handshake.SecretKeyMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.security.EncryptionManager;
import com.cometproject.server.network.sessions.Session;

import java.math.BigInteger;

public class GenerateSecretKeyMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        String cipher = msg.readString();

        BigInteger sharedKey = EncryptionManager.get().calculateSharedKey(cipher);

        client.initEncryption(sharedKey.toByteArray());
        client.send(SecretKeyMessageComposer.compose(EncryptionManager.get().getPublicKey()));
    }
}
