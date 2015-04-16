package com.cometproject.server.network.messages.outgoing.room.items.lovelock;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class LoveLockConfirmedMessageComposer extends MessageComposer {
    private final int itemId;

    public LoveLockConfirmedMessageComposer(final int itemId) {
        this.itemId = itemId;
    }

    @Override
    public short getId() {
        return Composers.LoveLockConfirmedMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(itemId);
    }
}
