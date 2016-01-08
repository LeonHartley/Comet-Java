package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class RoomPanelMessageComposer extends MessageComposer {
    private final int id;
    private final boolean hasOwnershipPermission;

    public RoomPanelMessageComposer(final int id, final boolean hasOwnershipPermission) {
        this.id = id;
        this.hasOwnershipPermission = hasOwnershipPermission;
    }

    @Override
    public short getId() {
        return 0;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(id);
        msg.writeBoolean(hasOwnershipPermission);
    }
}
