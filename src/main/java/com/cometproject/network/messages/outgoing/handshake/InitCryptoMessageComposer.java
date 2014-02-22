package com.cometproject.network.messages.outgoing.handshake;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class InitCryptoMessageComposer {
    public static Composer compose(String key, boolean flag) {
        Composer msg = new Composer(Composers.InitCryptoMessageComposer);

        msg.writeString(key);
        msg.writeBoolean(flag);

        return msg;
    }
}
