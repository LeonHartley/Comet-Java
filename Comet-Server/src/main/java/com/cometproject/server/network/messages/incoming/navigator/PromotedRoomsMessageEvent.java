package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.rooms.IRoomData;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.types.RoomPromotion;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.navigator.NavigatorFlatListMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.google.common.collect.Lists;

import java.util.List;


public class PromotedRoomsMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        List<IRoomData> promotedRooms = Lists.newArrayList();

        for (RoomPromotion roomPromotion : RoomManager.getInstance().getRoomPromotions().values()) {
            if (roomPromotion != null) {
                IRoomData roomData = GameContext.getCurrent().getRoomService().getRoomData(roomPromotion.getRoomId());

                if (roomData != null) {
                    promotedRooms.add(roomData);
                }
            }
        }


        client.send(new NavigatorFlatListMessageComposer(0, "", promotedRooms));
    }
}
