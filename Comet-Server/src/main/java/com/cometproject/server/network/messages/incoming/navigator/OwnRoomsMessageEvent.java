package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.rooms.IRoomData;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.navigator.NavigatorFlatListMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

import java.util.LinkedList;
import java.util.List;


public class OwnRoomsMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        List<IRoomData> rooms = new LinkedList<>();

        for (Integer roomId : new LinkedList<>(client.getPlayer().getRooms())) {
            if (GameContext.getCurrent().getRoomService().getRoomData(roomId)== null) continue;

            rooms.add(GameContext.getCurrent().getRoomService().getRoomData(roomId));
        }

        client.send(new NavigatorFlatListMessageComposer(5, "", rooms, false, false));
    }
}
