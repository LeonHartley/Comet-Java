package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.types.RoomDataInstance;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.navigator.NavigatorFlatListMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;

import java.util.ArrayList;
import java.util.List;


public class OwnRoomsMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        List<RoomDataInstance> rooms = new ArrayList<>();

        for (Integer roomId : new ArrayList<>(client.getPlayer().getRooms())) {
            if (RoomManager.getInstance().getRoomData(roomId) == null) continue;

            rooms.add(RoomManager.getInstance().getRoomData(roomId));
        }

        client.send(new NavigatorFlatListMessageComposer(5, "", rooms, false, false));
    }
}
