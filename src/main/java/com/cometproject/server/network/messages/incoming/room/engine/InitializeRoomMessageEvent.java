package com.cometproject.server.network.messages.incoming.room.engine;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class InitializeRoomMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int id = msg.readInt();
        String password = msg.readString();

        client.getPlayer().loadRoom(id, password);
    }
}
