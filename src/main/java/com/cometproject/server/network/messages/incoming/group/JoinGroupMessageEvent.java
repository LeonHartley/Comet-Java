package com.cometproject.server.network.messages.incoming.group;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class JoinGroupMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int groupId = msg.readInt();

        if(client.getPlayer().getGroups().contains(groupId)) {
            // Already joined, what you doing??
            return;
        }


    }
}
