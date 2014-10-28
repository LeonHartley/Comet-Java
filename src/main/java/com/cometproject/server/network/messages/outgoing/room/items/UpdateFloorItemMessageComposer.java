package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class UpdateFloorItemMessageComposer {
    public static Composer compose(RoomItemFloor item, int ownerId) {
        Composer msg = new Composer(Composers.UpdateFloorItemExtraDataMessageComposer);

        item.serialize(msg);
        msg.writeInt(ownerId);

        return msg;
    }

    public static Composer compose(RoomItemFloor item) {
        Composer msg = new Composer(Composers.UpdateFloorItemExtraDataMessageComposer);

        item.serialize(msg);
        msg.writeInt(item.getRoom().getData().getOwnerId());

        return msg;
    }
}
