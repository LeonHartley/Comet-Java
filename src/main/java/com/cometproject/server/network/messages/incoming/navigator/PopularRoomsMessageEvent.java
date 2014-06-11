package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.navigator.NavigatorFlatListMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

import java.util.ArrayList;
import java.util.List;

public class PopularRoomsMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int categoryId = Integer.parseInt(msg.readString());

        client.send(NavigatorFlatListMessageComposer.compose(categoryId, 2, "", CometManager.getRooms().getRoomsByCategory(categoryId)));
    }
}
