package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.game.rooms.types.RoomPromotion;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.navigator.NavigatorFlatListMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class PromotedRoomsMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        List<RoomData> promotedRooms = Lists.newArrayList();

        for(RoomPromotion roomPromotion : CometManager.getRooms().getRoomPromotions().values()) {
            if(roomPromotion != null) {
                RoomData roomData = CometManager.getRooms().getRoomData(roomPromotion.getRoomId());

                if(roomData != null) {
                    promotedRooms.add(roomData);
                }
            }
        }


        client.send(NavigatorFlatListMessageComposer.compose(-1, 0, "", promotedRooms));
    }
}
