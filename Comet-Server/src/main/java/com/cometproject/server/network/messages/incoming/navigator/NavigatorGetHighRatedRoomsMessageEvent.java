package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.navigator.NavigatorFlatListMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.rooms.RoomDao;


public class NavigatorGetHighRatedRoomsMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        client.send(new NavigatorFlatListMessageComposer(0, "", RoomDao.getHighestScoredRooms(), true, true));
    }
}
