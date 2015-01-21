package com.cometproject.server.network.messages.outgoing.misc;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class PingMessageComposer {
    public static Composer compose() {
        return new Composer(Composers.PingMessageComposer);
    }
}
