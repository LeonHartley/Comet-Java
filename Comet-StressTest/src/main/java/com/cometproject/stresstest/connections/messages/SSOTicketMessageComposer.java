package com.cometproject.stresstest.connections.messages;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Events;

public class SSOTicketMessageComposer extends MessageComposer {

    private final String ssoTicket;

    public SSOTicketMessageComposer(String ssoTicket) {
        this.ssoTicket = ssoTicket;
    }

    @Override
    public short getId() {
        return Events.SSOTicketMessageEvent;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(this.ssoTicket);
    }
}
