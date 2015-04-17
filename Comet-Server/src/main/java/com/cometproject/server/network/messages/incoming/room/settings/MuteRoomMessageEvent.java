package com.cometproject.server.network.messages.incoming.room.settings;

import com.cometproject.server.game.rooms.types.RoomInstance;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class MuteRoomMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        if (client.getPlayer().getEntity() == null)
            return;

        RoomInstance room = client.getPlayer().getEntity().getRoom();

        if (room.getData().getOwnerId() != client.getPlayer().getId() && !client.getPlayer().getPermissions().hasPermission("room_full_control")) {
            return;
        }

        if (room.hasRoomMute()) {
            room.setRoomMute(false);
        } else {
            room.setRoomMute(true);
        }
    }
}
