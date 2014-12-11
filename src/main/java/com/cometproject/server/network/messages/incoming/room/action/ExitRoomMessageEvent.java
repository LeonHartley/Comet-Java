package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.engine.HotelViewMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class ExitRoomMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        if (client.getPlayer() == null || client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            client.send(HotelViewMessageComposer.compose());
            return;
        }

        client.getPlayer().getEntity().leaveRoom(false, false, true);
    }
}
