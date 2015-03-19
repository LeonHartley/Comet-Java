package com.cometproject.server.network.messages.incoming.room.filter;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class EditWordFilterMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {

    }
}
