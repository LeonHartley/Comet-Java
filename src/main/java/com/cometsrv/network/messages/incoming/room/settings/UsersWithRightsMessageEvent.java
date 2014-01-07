package com.cometsrv.network.messages.incoming.room.settings;

import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.room.settings.GetPowerListMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class UsersWithRightsMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        Room room = client.getPlayer().getEntity().getRoom();

        if(room == null) {
            return;
        }

        client.send(GetPowerListMessageComposer.compose(room.getId(), room.getRights().getAll()));
    }
}
