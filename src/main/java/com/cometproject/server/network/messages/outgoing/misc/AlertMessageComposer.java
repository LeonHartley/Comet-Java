package com.cometproject.server.network.messages.outgoing.misc;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class AlertMessageComposer {
    public static Composer compose(String message) {
        Composer msg = new Composer(Composers.AlertMessageComposer);

        msg.writeString(message);

        return msg;
    }
}
