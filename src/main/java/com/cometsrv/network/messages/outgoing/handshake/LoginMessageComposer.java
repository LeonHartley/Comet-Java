package com.cometsrv.network.messages.outgoing.handshake;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class LoginMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.LoginMessageComposer);

        return msg;
    }
}
