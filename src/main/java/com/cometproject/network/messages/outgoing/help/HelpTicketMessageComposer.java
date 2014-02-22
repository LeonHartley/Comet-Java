package com.cometproject.network.messages.outgoing.help;

import com.cometproject.game.moderation.types.HelpTicket;
import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class HelpTicketMessageComposer {
    public static Composer compose(HelpTicket ticket) {
        Composer msg = new Composer(Composers.HelpTicketMessageComposer);



        return msg;
    }
}
