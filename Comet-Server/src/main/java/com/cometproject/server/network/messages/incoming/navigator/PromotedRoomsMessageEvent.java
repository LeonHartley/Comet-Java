package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.game.rooms.types.RoomPromotion;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.navigator.NavigatorFlatListMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.google.common.collect.Lists;

import java.util.List;


public class PromotedRoomsMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        List<RoomData> promotedRooms = Lists.newArrayList();

        for (RoomPromotion roomPromotion : RoomManager.getInstance().getRoomPromotions().values()) {
            if (roomPromotion != null) {
                RoomData roomData = RoomManager.getInstance().getRoomData(roomPromotion.getRoomId());

                if (roomData != null) {
                    promotedRooms.add(roomData);
                }
            }
        }


        client.send(new NavigatorFlatListMessageComposer(0, "", promotedRooms));
    }
}
