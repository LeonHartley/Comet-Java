package com.cometproject.server.network.messages.outgoing.user.details;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class EnableNotificationsMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.EnableNotificationsMessageComposer);

        msg.writeBoolean(true);
        msg.writeBoolean(false);

        return msg;
    }
}
