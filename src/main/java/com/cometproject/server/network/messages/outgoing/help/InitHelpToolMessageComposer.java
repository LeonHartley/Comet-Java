package com.cometproject.server.network.messages.outgoing.help;

import com.cometproject.server.game.moderation.types.tickets.HelpTicket;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class InitHelpToolMessageComposer extends MessageComposer {
    private final HelpTicket helpTicket;

    public InitHelpToolMessageComposer(HelpTicket helpTicket) {
        this.helpTicket = helpTicket;
    }

    @Override
    public short getId() {
        return Composers.InitHelpToolMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(this.helpTicket == null ? 0 : 1);

        if(this.helpTicket != null) {
            msg.writeString(this.helpTicket.getId());
            msg.writeString(this.helpTicket.getDateSubmitted());
            msg.writeString(this.helpTicket.getMessage());
        }
    }
}
