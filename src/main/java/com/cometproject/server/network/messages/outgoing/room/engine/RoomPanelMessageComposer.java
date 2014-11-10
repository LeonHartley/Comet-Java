package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class RoomPanelMessageComposer {
    public static Composer compose(int id, boolean hasOwnershipPermission) {
        Composer msg = new Composer(Composers.RoomOwnershipMessageComposer);
        msg.writeInt(id);
        msg.writeBoolean(hasOwnershipPermission);
        return msg;
    }
}
