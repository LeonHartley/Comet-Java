package com.cometproject.server.network.messages.outgoing.room.permissions;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class YouAreControllerMessageComposer extends MessageComposer {
    private final int rightId;

    public YouAreControllerMessageComposer(int rightId) {
        this.rightId = rightId;
    }

    @Override
    public short getId() {
        return Composers.YouAreControllerMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(rightId);
    }
}
