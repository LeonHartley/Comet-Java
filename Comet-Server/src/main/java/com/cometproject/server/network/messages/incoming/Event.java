package com.cometproject.server.network.messages.incoming;

import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public interface Event {
    void handle(Session client, MessageEvent msg) throws Exception;
}
