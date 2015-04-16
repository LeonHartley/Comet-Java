package com.cometproject.server.network.messages.incoming;

import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public interface Event {
    public void handle(Session client, MessageEvent msg) throws Exception;
}
