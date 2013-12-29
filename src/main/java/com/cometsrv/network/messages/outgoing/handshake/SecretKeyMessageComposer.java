package com.cometsrv.network.messages.outgoing.handshake;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class SecretKeyMessageComposer {
    public static Composer compose(String key) {
        Composer msg = new Composer(Composers.SecretKeyMessageComposer);

        msg.writeString(key);

        return msg;
    }
}
