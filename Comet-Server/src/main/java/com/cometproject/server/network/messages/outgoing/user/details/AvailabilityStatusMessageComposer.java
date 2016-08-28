package com.cometproject.server.network.messages.outgoing.user.details;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class AvailabilityStatusMessageComposer extends MessageComposer {

    @Override
    public short getId() {
        return Composers.AvailabilityStatusMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeBoolean(false);
        msg.writeBoolean(false);
        msg.writeBoolean(true);
    }
}
