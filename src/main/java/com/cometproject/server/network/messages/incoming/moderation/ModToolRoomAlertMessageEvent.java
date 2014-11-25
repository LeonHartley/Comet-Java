package com.cometproject.server.network.messages.incoming.moderation;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class ModToolRoomAlertMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int action = msg.readInt();

        String alert = msg.readString();
        String reason = msg.readString();

        if (!client.getPlayer().getPermissions().hasPermission("mod_tool")) {
            client.disconnect();
            return;
        }
    }
}
