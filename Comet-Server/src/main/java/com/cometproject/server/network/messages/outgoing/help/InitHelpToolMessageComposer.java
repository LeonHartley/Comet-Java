package com.cometproject.server.network.messages.outgoing.help;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.moderation.types.tickets.HelpTicket;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class InitHelpToolMessageComposer extends MessageComposer {
    private final HelpTicket helpTicket;

    public InitHelpToolMessageComposer(HelpTicket helpTicket) {
        this.helpTicket = helpTicket;
    }

    @Override
    public short getId() {
        return Composers.CallForHelpPendingCallsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.helpTicket == null ? 0 : 1);

        if (this.helpTicket != null) {
            msg.writeString(this.helpTicket.getId());
            msg.writeString(this.helpTicket.getDateSubmitted());
            msg.writeString(this.helpTicket.getMessage());
        }
    }
}
