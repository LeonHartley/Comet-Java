package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.navigator.NavigatorFlatListMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

import java.util.ArrayList;
import java.util.List;

public class OwnRoomsMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        List<RoomData> rooms = new ArrayList<>();

        for (Integer roomId : client.getPlayer().getRooms()) {
            if(CometManager.getRooms().getRoomData(roomId) == null) continue;

            rooms.add(CometManager.getRooms().getRoomData(roomId));
        }

        client.send(NavigatorFlatListMessageComposer.compose(0, 5, "", rooms, false, false));
    }
}
