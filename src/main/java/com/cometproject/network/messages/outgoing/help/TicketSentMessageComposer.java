package com.cometproject.network.messages.outgoing.help;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class TicketSentMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.TicketSentMessageComposer);

        msg.writeInt(0);

        return msg;
    }
}
