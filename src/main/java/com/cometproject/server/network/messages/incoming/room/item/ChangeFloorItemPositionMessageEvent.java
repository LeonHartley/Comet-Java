package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.rooms.entities.misc.Position3D;
import com.cometproject.server.game.rooms.entities.pathfinding.AffectedTile;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.mapping.TileInstance;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ChangeFloorItemPositionMessageEvent implements IEvent {
    private static Logger log = Logger.getLogger(ChangeFloorItemPositionMessageEvent.class);

    public void handle(Session client, Event msg) {
        int id = msg.readInt();
        int x = msg.readInt();
        int y = msg.readInt();
        int rot = msg.readInt();

        Room room = client.getPlayer().getEntity().getRoom();

        boolean isOwner = client.getPlayer().getId() == room.getData().getOwnerId();
        boolean hasRights = room.getRights().hasRights(client.getPlayer().getId());

        if ((isOwner || hasRights) || client.getPlayer().getPermissions().hasPermission("room_full_control")) {
            try {
                if(room.getItems().moveFloorItem(id, new Position3D(x, y), rot, true)) {}

                RoomItemFloor floorItem = room.getItems().getFloorItem(id);

                if(floorItem != null) {
                    room.getEntities().broadcastMessage(UpdateFloorItemMessageComposer.compose(floorItem, room.getData().getOwnerId()));
                }

            } catch (Exception e) {
                log.fatal("Error whilst changing floor item position!", e);
            }
        }
    }
}