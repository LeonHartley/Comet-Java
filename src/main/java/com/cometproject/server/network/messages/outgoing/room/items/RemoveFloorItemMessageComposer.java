package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class RemoveFloorItemMessageComposer extends MessageComposer {
    private final int id;
    private final int ownerId;

    public RemoveFloorItemMessageComposer(final int id, final int ownerId) {
        this.id = id;
        this.ownerId = ownerId;
    }

    @Override
    public short getId() {
        return Composers.PickUpFloorItemMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(this.id);
        msg.writeBoolean(false);
        msg.writeInt(this.ownerId);
        msg.writeInt(0);
    }
}
