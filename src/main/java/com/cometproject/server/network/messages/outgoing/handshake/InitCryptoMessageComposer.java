package com.cometproject.server.network.messages.outgoing.handshake;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class InitCryptoMessageComposer {
    public static Composer compose(String prime, String generator) {
        Composer msg = new Composer(Composers.InitCryptoMessageComposer);

        msg.writeString(prime);
        msg.writeString(generator);

        return msg;
    }
}
