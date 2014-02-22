package com.cometproject.network.messages.outgoing.room.items;

import com.cometproject.game.rooms.items.FloorItem;
import com.cometproject.game.rooms.types.Room;
import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class SendFloorItemMessageComposer {
    public static Composer compose(FloorItem item, Room room) {
        Composer msg = new Composer(Composers.SendFloorItemMessageComposer);

        item.serialize(msg);
        msg.writeInt(item.getOwner());
        msg.writeString(room.getData().getOwner());
        return msg;
    }
}
