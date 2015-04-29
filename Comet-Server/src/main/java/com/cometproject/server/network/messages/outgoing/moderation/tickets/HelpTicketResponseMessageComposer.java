package com.cometproject.server.network.messages.outgoing.moderation.tickets;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;

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
    public void compose(IComposer msg) {
        msg.writeInt(this.response);
    }
}
