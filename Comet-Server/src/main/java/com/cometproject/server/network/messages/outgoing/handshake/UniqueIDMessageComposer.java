package com.cometproject.server.network.messages.outgoing.handshake;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class UniqueIDMessageComposer extends MessageComposer {
    private final String uniqueId;

    public UniqueIDMessageComposer(final String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public short getId() {
        return Composers.SetUniqueIdMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(this.uniqueId);
    }
}
