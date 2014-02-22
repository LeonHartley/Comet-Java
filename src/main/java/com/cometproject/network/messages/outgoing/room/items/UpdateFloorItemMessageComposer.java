package com.cometproject.network.messages.outgoing.room.items;

import com.cometproject.game.rooms.items.FloorItem;
import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class UpdateFloorItemMessageComposer {
    public static Composer compose(FloorItem item, int ownerId) {
        Composer msg = new Composer(Composers.UpdateFloorItemMessageComposer);

        item.serialize(msg);
        msg.writeInt(ownerId);

        return msg;
    }
}
