package com.cometsrv.network.messages.incoming.room.settings;

import com.cometsrv.game.GameEngine;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.room.settings.GetPowerListMessageComposer;
import com.cometsrv.network.messages.outgoing.room.settings.GetRoomDataMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class LoadRoomInfoMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int roomId = msg.readInt();

        Room room = GameEngine.getRooms().get(roomId);

        if(room == null) {
            return;
        }

        client.send(GetRoomDataMessageComposer.compose(room));
        client.send(GetPowerListMessageComposer.compose(room.getId(), room.getRights().getAll()));
    }
}
