package com.cometproject.server.network.messages.outgoing.catalog.ads;

import com.cometproject.server.game.rooms.types.RoomDataInstance;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;


public class CatalogPromotionGetRoomsMessageComposer extends MessageComposer {
    private final List<RoomDataInstance> promotableRooms;

    public CatalogPromotionGetRoomsMessageComposer(final List<RoomDataInstance> rooms) {
        this.promotableRooms = rooms;
    }

    @Override
    public short getId() {
        return Composers.CatalogPromotionGetRoomsMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeBoolean(false);
        msg.writeInt(this.promotableRooms.size());

        for (RoomDataInstance data : this.promotableRooms) {
            msg.writeInt(data.getId());
            msg.writeString(data.getName());
            msg.writeBoolean(false);
        }
    }
}
