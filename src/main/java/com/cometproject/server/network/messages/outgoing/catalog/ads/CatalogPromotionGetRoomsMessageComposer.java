package com.cometproject.server.network.messages.outgoing.catalog.ads;

import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;

public class CatalogPromotionGetRoomsMessageComposer {
    public static Composer compose(List<RoomData> rooms) {
        Composer msg = new Composer(Composers.CatalogPromotionGetRoomsMessageComposer);

        msg.writeBoolean(false);
        msg.writeInt(rooms.size());

        for(RoomData data : rooms) {
            msg.writeInt(data.getId());
            msg.writeString(data.getName());
            msg.writeBoolean(false);
        }

        return msg;
    }
}
