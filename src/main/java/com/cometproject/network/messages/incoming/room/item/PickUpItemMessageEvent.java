package com.cometproject.network.messages.incoming.room.item;

import com.cometproject.game.rooms.items.FloorItem;
import com.cometproject.game.rooms.items.WallItem;
import com.cometproject.game.rooms.types.Room;
import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class PickUpItemMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        msg.readInt();

        int id = msg.readInt();
        Room room = client.getPlayer().getEntity().getRoom();

        if(room == null || !client.getPlayer().getData().getUsername().equals(client.getPlayer().getEntity().getRoom().getData().getOwner())) {
            return;
        }

        FloorItem item = room.getItems().getFloorItem(id);

        if(item == null) {
            WallItem wItem = room.getItems().getWallItem(id);

            if(wItem == null) {
                return;
            }

            room.getItems().removeItem(wItem, client);
            return;
        }

        room.getItems().removeItem(item, client);
    }
}
