package com.cometproject.server.network.messages.outgoing.moderation.tickets;

import com.cometproject.server.game.moderation.types.tickets.HelpTicket;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class HelpTicketMessageComposer extends MessageComposer {
    private final HelpTicket helpTicket;

    public HelpTicketMessageComposer(HelpTicket helpTicket) {
        this.helpTicket = helpTicket;;
    }

    @Override
    public short getId() {
        return Composers.HelpTicketMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        this.helpTicket.compose(msg);
    }
}
