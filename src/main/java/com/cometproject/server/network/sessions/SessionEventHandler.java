package com.cometproject.server.network.sessions;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.messages.headers.Events;
import com.cometproject.server.network.messages.types.Event;

import java.util.HashMap;
import java.util.Map;

public class SessionEventHandler {
    private final Session session;
    private final Map<Short, Boolean> loginEvents;

    public SessionEventHandler(Session session) {
        this.session = session;

        this.loginEvents = new HashMap<Short, Boolean>() {{
            put(Events.GenerateSecretKeyMessageEvent, true);
            put(Events.InitCryptoMessageEvent, true);
            put(Events.SSOTicketMessageEvent, true);
        }};
    }

    public void handle(Event msg) {
        // Checks if the event is a login event, if it is then check if it is enabled, if it is then disable it because the
        // event can only be called once!
        if (this.loginEvents.containsKey(msg.getId())) {
            if (!this.loginEvents.get(msg.getId())) {
                return; // event not allowed
            } else {
                this.loginEvents.replace(msg.getId(), false);
            }
        }

        Comet.getServer().getNetwork().getMessages().handle(msg, this.session);
    }
}
