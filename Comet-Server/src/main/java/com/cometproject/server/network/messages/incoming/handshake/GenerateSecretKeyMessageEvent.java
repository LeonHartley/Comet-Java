package com.cometproject.server.network.messages.incoming.handshake;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.handshake.SecretKeyMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class GenerateSecretKeyMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) {
        client.send(new SecretKeyMessageComposer("498611004c3cb9690bb515b1efb8226e6f4ab963b21f5956f7ce0651553c9b612c0f31727679479db75eb321e893e0c46763bacf37e10a8df9a48df4f2a5039093742942a5310831bc4ed29dac45c37954bc731a42d4a38d1d8c17c9b95549dd76334f0feaff33c655557b50ec2a9d09260c4c550f6f1aef7bf9f7f72e2c3a3f"));
    }
}
