package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class SendFloorItemMessageComposer {
    public static Composer compose(RoomItemFloor item, Room room) {
        Composer msg = new Composer(Composers.AddFloorItemMessageComposer);

        item.serialize(msg, true);
        //msg.writeInt(item.getOwner());
        //msg.writeString(room.getData().getOwner());
        return msg;
    }
}
