package com.cometproject.server.network.messages.incoming.room.settings;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class MuteRoomMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        if(client.getPlayer().getEntity() == null)
            return;

        Room room = client.getPlayer().getEntity().getRoom();

        if(room.getData().getOwnerId() != client.getPlayer().getId() && !client.getPlayer().getPermissions().hasPermission("room_full_control")) {
            return;
        }

        if(room.hasRoomMute()) {
            room.setRoomMute(false);
        } else {
            room.setRoomMute(true);
        }
    }
}
