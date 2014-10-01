package com.cometproject.server.network.messages.incoming.room.item.wired;

import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredItemSnapshot;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class UpdateSnapshotsMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int itemId = msg.readInt();

        if(client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) return;

        Room room = client.getPlayer().getEntity().getRoom();

        boolean isOwner = client.getPlayer().getId() == room.getData().getOwnerId();
        boolean hasRights = room.getRights().hasRights(client.getPlayer().getId());

        if ((!isOwner && !hasRights) && !client.getPlayer().getPermissions().hasPermission("room_full_control")) {
            return;
        }

        RoomItemFloor floorItem = room.getItems().getFloorItem(itemId);

        if(floorItem == null || !(floorItem instanceof WiredItemSnapshot.Refreshable)) {
            return;
        }

        ((WiredItemSnapshot.Refreshable) floorItem).refreshSnapshots();
    }
}
