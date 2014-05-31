package com.cometproject.server.network.sessions;

import com.cometproject.server.network.messages.types.Event;

import java.util.HashMap;
import java.util.Map;

public class SessionEventHandler {
    private final Session session;
    private final Map<Integer, Boolean> enabledEvents;

    public SessionEventHandler(Session session) {
        this.session = session;
        this.enabledEvents = new HashMap<>();
    }

    public void handle(Event msg) {
        if (!this.enabledEvents.containsKey(msg.getId())) {
            return;
        }
    }
}
