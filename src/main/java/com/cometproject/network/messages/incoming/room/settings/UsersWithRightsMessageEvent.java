package com.cometproject.network.messages.incoming.room.settings;

import com.cometproject.game.rooms.types.Room;
import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.room.settings.GetPowerListMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class UsersWithRightsMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        Room room = client.getPlayer().getEntity().getRoom();

        if(room == null) {
            return;
        }

        client.send(GetPowerListMessageComposer.compose(room.getId(), room.getRights().getAll()));
    }
}
