package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class RoomEntryInfoMessageComposer extends MessageComposer {
    private final int id;
    private final boolean hasOwnershipPermission;

    public RoomEntryInfoMessageComposer(final int id, final boolean hasOwnershipPermission) {
        this.id = id;
        this.hasOwnershipPermission = hasOwnershipPermission;
    }

    @Override
    public short getId() {
        return Composers.RoomEntryInfoMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(id);
        msg.writeBoolean(hasOwnershipPermission);
    }
}
