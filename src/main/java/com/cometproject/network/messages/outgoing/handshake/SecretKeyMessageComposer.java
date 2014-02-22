package com.cometproject.network.messages.outgoing.handshake;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class SecretKeyMessageComposer {
    public static Composer compose(String key) {
        Composer msg = new Composer(Composers.SecretKeyMessageComposer);

        msg.writeString(key);

        return msg;
    }
}
