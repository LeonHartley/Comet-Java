package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.engine.HotelViewMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class ExitRoomMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        if (client.getPlayer() == null || client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            client.send(new HotelViewMessageComposer());
            return;
        }

        client.getPlayer().getEntity().leaveRoom(false, false, true);
    }
}
