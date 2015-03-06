package com.cometproject.server.network.messages.outgoing.user.inventory;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class EffectsInventoryMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.EffectsInventoryMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(0);// TODO: Effects inventory.
    }
}
