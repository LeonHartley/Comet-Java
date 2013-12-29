package com.cometsrv.network.messages.outgoing.help;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class TicketSentMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.TicketSentMessageComposer);

        msg.writeInt(0);

        return msg;
    }
}
