package com.cometproject.network.messages.outgoing.misc;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class AlertMessageComposer {
    public static Composer compose(String message) {
        Composer msg = new Composer(Composers.AlertMessageComposer);

        msg.writeString(message);

        return msg;
    }
}
