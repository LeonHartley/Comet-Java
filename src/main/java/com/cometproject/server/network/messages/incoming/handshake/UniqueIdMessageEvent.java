package com.cometproject.server.network.messages.incoming.handshake;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class UniqueIdMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        msg.readString();
        String id = msg.readString();

        System.out.println("Id: " + id);
    }
}
