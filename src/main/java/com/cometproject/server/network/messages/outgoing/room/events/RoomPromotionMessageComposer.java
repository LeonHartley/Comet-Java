package com.cometproject.server.network.messages.outgoing.room.events;

import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.game.rooms.types.RoomPromotion;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class RoomPromotionMessageComposer {
    public static Composer compose(RoomData roomData, RoomPromotion roomPromotion) {
        Composer msg = new Composer(Composers.RoomEventMessageComposer);

        msg.writeInt(roomData.getId());
        msg.writeInt(roomData.getOwnerId());
        msg.writeString(roomData.getOwner());

        msg.writeInt(1);
        msg.writeInt(1);

        msg.writeString(roomPromotion.getPromotionName());
        msg.writeString(roomPromotion.getPromotionDescription());
        msg.writeInt(0);

        msg.writeInt((int) ((roomPromotion.getTimestampFinish() - roomPromotion.getTimestampStart()) / 60));

        return msg;
    }
}
