package com.cometproject.server.network.messages.outgoing.notification;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class LogoutMessageComposer {
    public static Composer compose(String reason) {
        Composer msg = new Composer(Composers.LogoutMessageComposer);

        msg.writeInt(reason.equals("banned") ? 1 : 0);

        return msg;
    }
}
