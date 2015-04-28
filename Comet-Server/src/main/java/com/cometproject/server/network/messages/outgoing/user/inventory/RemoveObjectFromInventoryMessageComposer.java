package com.cometproject.server.network.messages.outgoing.user.inventory;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class RemoveObjectFromInventoryMessageComposer extends MessageComposer {
    private final int itemId;

    public RemoveObjectFromInventoryMessageComposer(final int itemId) {
        this.itemId = itemId;
    }

    @Override
    public short getId() {
        return Composers.RemoveInventoryObjectMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.itemId);
    }
}
