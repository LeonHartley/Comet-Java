package com.cometproject.server.network.messages.incoming.catalog.ads;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomPromotion;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.events.RoomPromotionMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class PromotionUpdateMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int id = msg.readInt();
        String promotionName = msg.readString();
        String promotionDescription = msg.readString();

        Room room = client.getPlayer().getEntity().getRoom();

        if (room == null || (room.getData().getOwnerId() != client.getPlayer().getId() && !client.getPlayer().getPermissions().hasPermission("room_full_control"))) {
            return;
        }

        RoomPromotion roomPromotion = room.getPromotion();

        if (roomPromotion != null) {
            roomPromotion.setPromotionName(promotionName);
            roomPromotion.setPromotionDescription(promotionDescription);

            room.getEntities().broadcastMessage(RoomPromotionMessageComposer.compose(room.getData(), roomPromotion));
        }
    }
}
