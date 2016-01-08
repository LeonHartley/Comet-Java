package com.cometproject.server.network.messages.outgoing.room.permissions;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class AccessLevelMessageComposer extends MessageComposer {
    private final int rightId;

    public AccessLevelMessageComposer(int rightId) {
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
