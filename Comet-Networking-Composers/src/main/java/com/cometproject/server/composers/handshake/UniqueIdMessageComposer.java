package com.cometproject.server.composers.handshake;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class UniqueIdMessageComposer extends MessageComposer {
    private final String uniqueId;

    public UniqueIdMessageComposer(final String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public short getId() {
        return Composers.UniqueMachineIDMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(this.uniqueId);
    }
}
