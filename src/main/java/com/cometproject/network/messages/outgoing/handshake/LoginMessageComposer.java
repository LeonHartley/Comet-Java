package com.cometproject.network.messages.outgoing.handshake;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class LoginMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.LoginMessageComposer);

        return msg;
    }
}
