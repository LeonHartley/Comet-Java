package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class RemoveFloorItemMessageComposer {
    public static Composer compose(int id, int ownerId) {
        Composer msg = new Composer(Composers.PickUpFloorItemMessageComposer);

        msg.writeString(id);
        msg.writeBoolean(false);
        msg.writeInt(ownerId);
        msg.writeInt(0);

        return msg;
    }
}
