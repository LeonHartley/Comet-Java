package com.cometsrv.network.messages.outgoing.help;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class InitHelpToolMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.InitHelpToolMessageComposer);

        msg.writeInt(0);

        return msg;
    }
}
