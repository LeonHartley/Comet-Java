package com.cometproject.server.network.messages.incoming.moderation;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class ModToolUserChatlogMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int userId = msg.readInt();

        if (!client.getPlayer().getPermissions().hasPermission("mod_tool")) {
            return;
        }


    }
}
