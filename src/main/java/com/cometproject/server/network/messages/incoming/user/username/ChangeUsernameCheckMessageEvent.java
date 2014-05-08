package com.cometproject.server.network.messages.incoming.user.username;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class ChangeUsernameCheckMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        String username = msg.readString();c
    }
}
