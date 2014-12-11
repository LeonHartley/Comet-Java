package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.navigator.NavigatorFlatListMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.rooms.RoomDao;


public class NavigatorGetHighRatedRoomsMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        client.send(NavigatorFlatListMessageComposer.compose(0, 0, "", RoomDao.getHighestScoredRooms(), true, true));
    }
}
