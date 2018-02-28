package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class PopularRoomsMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        int categoryId = Integer.parseInt(msg.readString());

//        client.send(new NavigatorFlatListMessageComposer(2, "", RoomManager.getInstance().getRoomsByCategory(categoryId, 50)));
    }
}
