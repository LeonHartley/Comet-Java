package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.server.game.rooms.items.WallItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class SendWallItemMessageComposer {
    public static Composer compose(WallItem item, Room room) {
        Composer msg = new Composer(Composers.SendWallItemMessageComposer);

        item.serialize(msg, room);

        return msg;
    }
}
