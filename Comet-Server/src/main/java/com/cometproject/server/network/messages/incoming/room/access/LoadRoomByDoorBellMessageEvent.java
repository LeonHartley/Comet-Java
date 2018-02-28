package com.cometproject.server.network.messages.incoming.room.access;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class LoadRoomByDoorBellMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        if (client.getPlayer().getEntity() == null) {
            return;
        }

        // re-do room checks like max player check etc
        client.getPlayer().getEntity().joinRoom(client.getPlayer().getEntity().getRoom(), "");
    }
}
