package com.cometproject.network.messages.outgoing.help;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class InitHelpToolMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.InitHelpToolMessageComposer);

        msg.writeInt(0);

        return msg;
    }
}
