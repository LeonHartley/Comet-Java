package com.cometproject.server.network.messages.incoming.room.filter;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class WordFilterListMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        if(client.getPlayer().getEntity() == null) {
            return;
        }

        final Room room = client.getPlayer().getEntity().getRoom();

        if(room == null) {
            return;
        }
    }
}
