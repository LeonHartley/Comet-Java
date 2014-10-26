package com.cometproject.server.network.messages.outgoing.handshake;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class SecretKeyMessageComposer {
    public static Composer compose(String key) {
        Composer msg = new Composer(Composers.SecretKeyMessageComposer);

        msg.writeString(key);
        msg.writeBoolean(false);//rc4 clientside

        return msg;
    }
}
