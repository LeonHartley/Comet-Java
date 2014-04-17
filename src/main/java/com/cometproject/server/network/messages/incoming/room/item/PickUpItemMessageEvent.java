package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.items.WallItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class PickUpItemMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        msg.readInt();

        int id = msg.readInt();
        Room room = client.getPlayer().getEntity().getRoom();

        if (room == null || !room.getRights().hasRights(client.getPlayer().getId()) && !client.getPlayer().getPermissions().hasPermission("room_full_control")) {
            return;
        }

        FloorItem item = room.getItems().getFloorItem(id);

        if (item == null) {
            WallItem wItem = room.getItems().getWallItem(id);

            if (wItem == null) {
                return;
            }

            room.getItems().removeItem(wItem, client);
            return;
        }

        room.getItems().removeItem(item, client);
    }
}
