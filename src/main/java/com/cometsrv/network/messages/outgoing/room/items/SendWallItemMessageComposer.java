package com.cometsrv.network.messages.outgoing.room.items;

import com.cometsrv.game.rooms.items.WallItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class SendWallItemMessageComposer {
    public static Composer compose(WallItem item, Room room) {
        Composer msg = new Composer(Composers.SendWallItemMessageComposer);

        item.serialize(msg);
        msg.writeInt(room.getData().getOwnerId());

        return msg;
    }
}
