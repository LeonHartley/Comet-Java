package com.cometproject.server.network.messages.incoming.group.forum.settings;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class SaveForumSettingsMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int groupId = msg.readInt();

        for(int i = 0; i < 4; i++) {
            System.out.println(msg.readInt());
        }
    }
}
