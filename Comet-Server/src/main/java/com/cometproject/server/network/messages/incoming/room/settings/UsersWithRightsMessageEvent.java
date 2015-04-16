package com.cometproject.server.network.messages.incoming.room.settings;

import com.cometproject.server.game.rooms.types.RoomInstance;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.settings.RightsListMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class UsersWithRightsMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        RoomInstance room = client.getPlayer().getEntity().getRoom();

        if (room == null) {
            return;
        }

        client.send(new RightsListMessageComposer(room.getId(), room.getRights().getAll()));
    }
}
