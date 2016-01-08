package com.cometproject.server.network.messages.outgoing.room.access;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class DoorbellAcceptedComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.FlatAccessibleMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString("");
    }
}
