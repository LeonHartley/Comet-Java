package com.cometsrv.network.messages.incoming.room.item;

import com.cometsrv.game.rooms.avatars.misc.Position3D;
import com.cometsrv.game.rooms.avatars.pathfinding.AffectedTile;
import com.cometsrv.game.rooms.entities.GenericEntity;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.items.WallItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;
import javolution.util.FastList;

import java.util.List;

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
