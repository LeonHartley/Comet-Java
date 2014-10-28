package com.cometproject.server.network.messages.outgoing.help;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class InitHelpToolMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.OpenHelpToolMessageComposer);

        msg.writeInt(0);

        return msg;
    }
}
