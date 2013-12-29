package com.cometsrv.network.messages.outgoing.room.items;

import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class SendFloorItemMessageComposer {
    public static Composer compose(FloorItem item, Room room) {
        Composer msg = new Composer(Composers.SendFloorItemMessageComposer);

        item.serialize(msg);
        msg.writeInt(item.getOwner());
        msg.writeString(room.getData().getOwner());
        return msg;
    }
}
