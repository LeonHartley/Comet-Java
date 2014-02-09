package com.cometsrv.network.messages.incoming.handshake;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.handshake.SecretKeyMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.security.HabboEncryption;
import com.cometsrv.network.sessions.Session;

public class GenerateSecretKeyMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        String cipher = msg.readString();

        client.setEncryption(new HabboEncryption(HabboEncryption.N, HabboEncryption.E, HabboEncryption.D));
        client.getEncryption().initialize(cipher);

        client.send(SecretKeyMessageComposer.compose(client.getEncryption().getPublicKey()));
    }
}
