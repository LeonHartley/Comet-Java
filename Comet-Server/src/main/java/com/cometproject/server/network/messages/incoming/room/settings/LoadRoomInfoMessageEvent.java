package com.cometproject.server.network.messages.incoming.room.settings;

import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.types.RoomInstance;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.settings.GetRoomDataMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class LoadRoomInfoMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        int roomId = msg.readInt();

        RoomInstance room = RoomManager.getInstance().get(roomId);

        if (room == null) {
            return;
        }

        client.send(new GetRoomDataMessageComposer(room, client.getPlayer().getPermissions().hasPermission("mod_tool")));
    }
}
