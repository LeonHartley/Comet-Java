package com.cometproject.server.network.messages.outgoing.room.items.lovelock;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class LoveLockCloseWidgetMessageComposer extends MessageComposer {
    private final int itemId;

    public LoveLockCloseWidgetMessageComposer(final int itemId) {
        this.itemId = itemId;
    }

    @Override
    public short getId() {
        return Composers.LoveLockCloseWidgetMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(this.itemId);
    }
}
