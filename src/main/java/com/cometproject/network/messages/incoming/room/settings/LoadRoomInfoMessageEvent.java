package com.cometproject.network.messages.incoming.room.settings;

import com.cometproject.game.GameEngine;
import com.cometproject.game.rooms.types.Room;
import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.room.settings.GetPowerListMessageComposer;
import com.cometproject.network.messages.outgoing.room.settings.GetRoomDataMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

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
