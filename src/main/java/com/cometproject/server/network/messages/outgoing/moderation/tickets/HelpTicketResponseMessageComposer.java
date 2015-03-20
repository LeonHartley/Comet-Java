package com.cometproject.server.network.messages.outgoing.moderation.tickets;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class HelpTicketResponseMessageComposer extends MessageComposer {

    private int response;

    public HelpTicketResponseMessageComposer(final int response) {
        this.response = response;
    }

    @Override
    public short getId() {
        return Composers.HelpTicketResponseMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(this.response);
    }
}
