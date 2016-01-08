package com.cometproject.server.network.messages.outgoing.user.inventory;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class EffectsInventoryMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.AvatarEffectsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(0);// TODO: Effects inventory.
    }
}
