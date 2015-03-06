package com.cometproject.server.network.messages.incoming.room.settings;

import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.settings.GetRoomDataMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class LoadRoomInfoMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int roomId = msg.readInt();

        Room room = RoomManager.getInstance().get(roomId);

        if (room == null) {
            return;
        }

        client.send(new GetRoomDataMessageComposer(room, client.getPlayer().getPermissions().hasPermission("mod_tool")));
    }
}
