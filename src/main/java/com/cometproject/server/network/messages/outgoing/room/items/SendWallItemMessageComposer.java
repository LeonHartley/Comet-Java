package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.server.game.rooms.items.RoomItemWall;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class SendWallItemMessageComposer {
    public static Composer compose(RoomItemWall item) {
        Composer msg = new Composer(Composers.SendWallItemMessageComposer);

        item.serialize(msg);

        return msg;
    }
}
