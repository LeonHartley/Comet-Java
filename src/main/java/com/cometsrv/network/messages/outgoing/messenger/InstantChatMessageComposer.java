package com.cometsrv.network.messages.outgoing.messenger;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class InstantChatMessageComposer {
    public static Composer compose(String message, int fromId) {
        Composer msg = new Composer(Composers.InstantChatMessageComposer);

        msg.writeInt(fromId);
        msg.writeString(message);
        msg.writeInt(0);

        return msg;
    }
}
