package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class RemoveWallItemMessageComposer extends MessageComposer {
    private final int itemId;
    private final int playerId;

    public RemoveWallItemMessageComposer(final int itemId, final int playerId) {
        this.itemId = itemId;
        this.playerId = playerId;
    }

    @Override
    public short getId() {
        return Composers.PickUpWallItemMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(this.itemId);
        msg.writeInt(this.playerId);
    }
}
