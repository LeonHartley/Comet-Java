package com.cometproject.server.network.messages.outgoing.misc;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

/**
 * Created by Matty on 26/04/2014.
 */
public class PingMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.PingMessageComposer);
        return msg;
    }
}
