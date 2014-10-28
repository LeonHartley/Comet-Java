package com.cometproject.server.network.messages.incoming.handshake;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.handshake.InitCryptoMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.security.EncryptionManager;
import com.cometproject.server.network.sessions.Session;

public class InitCryptoMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
//        client.send(InitCryptoMessageComposer.compose(EncryptionManager.get().getPrimeKey(), EncryptionManager.get().getGeneratorKey()));
        client.send(InitCryptoMessageComposer.compose("498611004c3cb9690bb515b1efb8226e6f4ab963b21f5956f7ce0651553c9b612c0f31727679479db75eb321e893e0c46763bacf37e10a8df9a48df4f2a5039093742942a5310831bc4ed29dac45c37954bc731a42d4a38d1d8c17c9b95549dd76334f0feaff33c655557b50ec2a9d09260c4c550f6f1aef7bf9f7f72e2c3a3f", "595d77d9b63234f7b8104a4bbe62f45de05715f48b56a1bae9429c57cd27e5b912313073dc1ffd2e980bf0dcb348e6d0d35230f291b902cecc129eae075901a6c681bcab96baa0453051637e2aa547b429e7409f72685e5ec15d6f210198a05a19f92e87ce9dc4abf435c0d5dc7edf0b4fbd499d02b6641734814c190cffedc0"));
    }
}
