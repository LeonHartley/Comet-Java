package com.cometproject.server.network.messages.outgoing.catalog.ads;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.List;


public class CatalogPromotionGetRoomsMessageComposer extends MessageComposer {
    private final List<RoomData> promotableRooms;

    public CatalogPromotionGetRoomsMessageComposer(final List<RoomData> rooms) {
        this.promotableRooms = rooms;
    }

    @Override
    public short getId() {
        return Composers.PromotableRoomsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeBoolean(false);
        msg.writeInt(this.promotableRooms.size());

        for (RoomData data : this.promotableRooms) {
            msg.writeInt(data.getId());
            msg.writeString(data.getName());
            msg.writeBoolean(false);
        }
    }
}
