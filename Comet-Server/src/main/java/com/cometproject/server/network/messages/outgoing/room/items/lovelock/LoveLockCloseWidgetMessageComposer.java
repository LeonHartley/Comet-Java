package com.cometproject.server.network.messages.outgoing.room.items.lovelock;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class LoveLockCloseWidgetMessageComposer extends MessageComposer {
    private final int itemId;

    public LoveLockCloseWidgetMessageComposer(final int itemId) {
        this.itemId = itemId;
    }

    @Override
    public short getId() {
        return Composers.LoveLockDialogueCloseMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.itemId);
    }
}
