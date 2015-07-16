package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.navigator.NavigatorFlatListMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;

import java.util.LinkedList;
import java.util.List;


public class OwnRoomsMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        List<RoomData> rooms = new LinkedList<>();

        for (Integer roomId : new LinkedList<>(client.getPlayer().getRooms())) {
            if (RoomManager.getInstance().getRoomData(roomId) == null) continue;

            rooms.add(RoomManager.getInstance().getRoomData(roomId));
        }

        client.send(new NavigatorFlatListMessageComposer(5, "", rooms, false, false));
    }
}
