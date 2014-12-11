package com.cometproject.server.network.messages.outgoing.help;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class TicketSentMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.TicketSentMessageComposer);

        msg.writeInt(0);

        return msg;
    }
}
