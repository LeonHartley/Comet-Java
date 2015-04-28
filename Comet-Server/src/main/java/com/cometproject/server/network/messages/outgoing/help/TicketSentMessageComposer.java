package com.cometproject.server.network.messages.outgoing.help;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


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
