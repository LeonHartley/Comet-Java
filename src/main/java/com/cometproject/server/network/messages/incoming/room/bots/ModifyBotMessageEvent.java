package com.cometproject.server.network.messages.incoming.room.bots;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class ModifyBotMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        throw new Exception("Feature not implemented");
    }
}
