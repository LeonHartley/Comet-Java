package com.cometproject.server.network.messages.outgoing.room.events;

import com.cometproject.api.game.rooms.IRoomData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.types.RoomPromotion;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class RoomPromotionMessageComposer extends MessageComposer {
    private final IRoomData roomData;
    private final RoomPromotion roomPromotion;

    public RoomPromotionMessageComposer(final IRoomData roomData, final RoomPromotion roomPromotion) {
        this.roomData = roomData;
        this.roomPromotion = roomPromotion;
    }

    @Override
    public short getId() {
        return Composers.RoomEventMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        if (roomData == null || roomPromotion == null) {
            msg.writeInt(-1);
            msg.writeInt(-1);
            msg.writeString("");
            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeString("");
            msg.writeString("");
            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeInt(0);
            return;
        }

        msg.writeInt(roomData.getId());
        msg.writeInt(roomData.getOwnerId());
        msg.writeString(roomData.getOwner());

        msg.writeInt(1);
        msg.writeInt(1);

        msg.writeString(roomPromotion.getPromotionName());
        msg.writeString(roomPromotion.getPromotionDescription());
        msg.writeInt(0);

        msg.writeInt((int) ((roomPromotion.getTimestampFinish() - roomPromotion.getTimestampStart()) / 60));
        msg.writeInt(0);

    }
}
