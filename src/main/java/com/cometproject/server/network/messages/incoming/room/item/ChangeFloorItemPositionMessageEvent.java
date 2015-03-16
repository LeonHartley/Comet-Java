package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import org.apache.log4j.Logger;


public class ChangeFloorItemPositionMessageEvent implements IEvent {
    private static Logger log = Logger.getLogger(ChangeFloorItemPositionMessageEvent.class);

    public void handle(Session client, Event msg) {
        int id = msg.readInt();
        int x = msg.readInt();
        int y = msg.readInt();
        int rot = msg.readInt();

        if (client.getPlayer().getEntity() == null) return;

        Room room = client.getPlayer().getEntity().getRoom();

        if (room == null) return;


        if (!client.getPlayer().getEntity().getRoom().getRights().hasRights(client.getPlayer().getId())
                && !client.getPlayer().getPermissions().hasPermission("room_full_control")) {
            return;
        }

        try {
            if (room.getItems().moveFloorItem(id, new Position(x, y), rot, true)) {
                // success!
            }

            RoomItemFloor floorItem = room.getItems().getFloorItem(id);

            if (floorItem != null) {
                room.getEntities().broadcastMessage(new UpdateFloorItemMessageComposer(floorItem));
            }
        } catch (Exception e) {
            log.error("Error whilst changing floor item position!", e);
        }
    }
}