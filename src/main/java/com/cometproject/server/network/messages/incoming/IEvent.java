package com.cometproject.server.network.messages.incoming;

import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public interface IEvent {
    public void handle(Session client, Event msg) throws Exception;
}
