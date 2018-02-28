package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class RemoveWallItemMessageComposer extends MessageComposer {
    private final int itemId;
    private final int playerId;

    public RemoveWallItemMessageComposer(final int itemId, final int playerId) {
        this.itemId = itemId;
        this.playerId = playerId;
    }

    @Override
    public short getId() {
        return Composers.ItemRemoveMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(this.itemId);
        msg.writeInt(this.playerId);
    }
}
