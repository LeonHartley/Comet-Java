package com.cometsrv.network.messages.outgoing.help;

import com.cometsrv.game.moderation.types.HelpTicket;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class HelpTicketMessageComposer {
    public static Composer compose(HelpTicket ticket) {
        Composer msg = new Composer(Composers.HelpTicketMessageComposer);



        return msg;
    }
}
