package com.cometproject.server.network.messages.outgoing.help;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;


public class TicketSentMessageComposer extends MessageComposer {
    public TicketSentMessageComposer() {

    }

    @Override
    public short getId() {
        return Composers.TicketSentMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(0);
    }
}
