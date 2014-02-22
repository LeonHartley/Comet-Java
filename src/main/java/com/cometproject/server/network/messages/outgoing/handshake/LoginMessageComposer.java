package com.cometproject.server.network.messages.outgoing.handshake;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class LoginMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.LoginMessageComposer);

        return msg;
    }
}
