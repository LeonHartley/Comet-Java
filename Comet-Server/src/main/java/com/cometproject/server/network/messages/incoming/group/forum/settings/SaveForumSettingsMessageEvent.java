package com.cometproject.server.network.messages.incoming.group.forum.settings;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;

public class SaveForumSettingsMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();

        for (int i = 0; i < 4; i++) {
            System.out.println(msg.readInt());
        }
    }
}
