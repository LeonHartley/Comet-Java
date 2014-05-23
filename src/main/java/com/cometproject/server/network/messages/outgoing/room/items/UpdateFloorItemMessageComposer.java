package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class UpdateFloorItemMessageComposer {
    public static Composer compose(RoomItemFloor item, int ownerId) {
        Composer msg = new Composer(Composers.UpdateFloorItemMessageComposer);

        item.serialize(msg);
        msg.writeInt(ownerId);

        return msg;
    }
}
