package com.cometsrv.network.messages.outgoing.misc;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class AlertMessageComposer {
    public static Composer compose(String message) {
        Composer msg = new Composer(Composers.AlertMessageComposer);

        msg.writeString(message);

        return msg;
    }
}
