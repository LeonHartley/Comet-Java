package com.cometsrv.network.messages.outgoing.room.items;

import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class UpdateFloorItemMessageComposer {
    public static Composer compose(FloorItem item, int ownerId) {
        Composer msg = new Composer(Composers.UpdateFloorItemMessageComposer);

        item.serialize(msg);
        msg.writeInt(ownerId);

        return msg;
    }
}
