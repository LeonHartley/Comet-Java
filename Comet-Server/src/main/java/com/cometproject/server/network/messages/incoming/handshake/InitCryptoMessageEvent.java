package com.cometproject.server.network.messages.incoming.handshake;

import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.handshake.InitCryptoMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class InitCryptoMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) {
        final String p = client.getDiffieHellman().getPrime().toString();
        final String g = client.getDiffieHellman().getGenerator().toString();

        String encP = NetworkManager.getInstance().getRSA().sign(p);
        String encG = NetworkManager.getInstance().getRSA().sign(g);

        client.send(new InitCryptoMessageComposer(encP, encG));
    }
}
