package com.cometproject.network.messages.outgoing.room.items;

import com.cometproject.game.rooms.items.WallItem;
import com.cometproject.game.rooms.types.Room;
import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class SendWallItemMessageComposer {
    public static Composer compose(WallItem item, Room room) {
        Composer msg = new Composer(Composers.SendWallItemMessageComposer);

        item.serialize(msg);
        msg.writeInt(room.getData().getOwnerId());

        return msg;
    }
}
