package com.cometsrv.network.messages.outgoing.user.details;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class WelcomeUserMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.WelcomeUserMessageComposer);

        msg.writeInt(0);

        return msg;
    }
}
