package com.cometproject.server.network.messages.incoming.catalog.ads;

import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.catalog.ads.CatalogPromotionGetRoomsMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.google.common.collect.Lists;

import java.util.List;


public class CatalogPromotionGetRoomsMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        List<RoomData> roomDataList = Lists.newArrayList();

        for (Integer roomId : client.getPlayer().getRooms()) {
            RoomData data = RoomManager.getInstance().getRoomData(roomId);

            if (data != null && data.getAccess().toLowerCase().equals("open")) {
                roomDataList.add(data);
            }
        }

        client.send(new CatalogPromotionGetRoomsMessageComposer(roomDataList));
    }
}