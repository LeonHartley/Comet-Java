package com.cometproject.server.network.messages.incoming.room.settings;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.settings.GetPowerListMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.settings.GetRoomDataMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class LoadRoomInfoMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int roomId = msg.readInt();

        Room room = GameEngine.getRooms().get(roomId);

        if (room == null) {
            return;
        }

        client.send(GetRoomDataMessageComposer.compose(room));
        client.send(GetPowerListMessageComposer.compose(room.getId(), room.getRights().getAll()));
    }
}
