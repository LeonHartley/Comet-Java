package com.cometproject.server.network.messages.outgoing.user.details;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class UnreadMinimailsMessageComposer {
    public static Composer compose(int count) {
        Composer msg = new Composer(Composers.MinimailCountMessageComposer);

        msg.writeInt(count);

        return msg;
    }
}
