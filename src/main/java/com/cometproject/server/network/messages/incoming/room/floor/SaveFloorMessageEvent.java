package com.cometproject.server.network.messages.incoming.room.floor;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class SaveFloorMessageEvent implements IEvent {

    @Override
    public void handle(Session client, Event msg) throws Exception {
        String model = msg.readString();

        Room room = client.getPlayer().getEntity().getRoom();

        if (room == null || (room.getData().getOwnerId() != client.getPlayer().getId() && !client.getPlayer().getPermissions().hasPermission("room_full_control"))) {
            return;
        }

        room.getData().setHeightmap(model);
    }
}
