package com.cometproject.server.network.messages.incoming.latency;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class RequestLatencyTestMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {

    }
}
