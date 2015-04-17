package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class RoomPanelMessageComposer extends MessageComposer {
    private final int id;
    private final boolean hasOwnershipPermission;

    public RoomPanelMessageComposer(final int id, final boolean hasOwnershipPermission) {
        this.id = id;
        this.hasOwnershipPermission = hasOwnershipPermission;
    }

    @Override
    public short getId() {
        return Composers.RoomOwnershipMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(id);
        msg.writeBoolean(hasOwnershipPermission);
    }
}
