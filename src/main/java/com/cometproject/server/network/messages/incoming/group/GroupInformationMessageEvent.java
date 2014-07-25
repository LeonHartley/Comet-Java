package com.cometproject.server.network.messages.incoming.group;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class GroupInformationMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {

    }
}
