package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;


public class HandItemMessageComposer extends MessageComposer {
    private final int avatarId;
    private final int handItemId;

    public HandItemMessageComposer(final int avatarId, final int handItemId) {
        this.avatarId = avatarId;
        this.handItemId = handItemId;
    }

    @Override
    public short getId() {
        return Composers.ApplyHanditemMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(avatarId);
        msg.writeInt(handItemId);
    }
}
