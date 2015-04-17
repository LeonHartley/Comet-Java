package com.cometproject.server.network.messages.incoming.room.filter;

import com.cometproject.server.game.rooms.types.RoomInstance;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;

public class WordFilterListMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        if(client.getPlayer().getEntity() == null) {
            return;
        }

        final RoomInstance room = client.getPlayer().getEntity().getRoom();

        if(room == null) {
            return;
        }
    }
}
