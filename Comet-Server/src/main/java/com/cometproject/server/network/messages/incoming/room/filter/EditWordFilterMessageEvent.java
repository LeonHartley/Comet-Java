package com.cometproject.server.network.messages.incoming.room.filter;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;

public class EditWordFilterMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {

    }
}
