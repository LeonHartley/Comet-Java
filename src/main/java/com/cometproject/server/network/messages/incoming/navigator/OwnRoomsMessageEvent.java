package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.navigator.NavigatorFlatListMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

import java.util.ArrayList;
import java.util.List;

public class OwnRoomsMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        List<Room> rooms = new ArrayList<>();

        for(Integer roomId : client.getPlayer().getRooms()) {
            rooms.add(GameEngine.getRooms().get(roomId));
        }

        client.send(NavigatorFlatListMessageComposer.compose(0, 5, "", rooms, false));
    }
}
