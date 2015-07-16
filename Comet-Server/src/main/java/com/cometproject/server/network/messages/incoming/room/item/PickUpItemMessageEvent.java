package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.quests.QuestType;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.objects.items.types.wall.PostItWallItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.items.RemoveWallItemMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class PickUpItemMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        if (client.getPlayer() == null || client.getPlayer().getEntity() == null) {
            return;
        }

        boolean isFloorItem = msg.readInt() == 2;

        int id = msg.readInt();
        Room room = client.getPlayer().getEntity().getRoom();

        if (room == null) {
            return;
        }

        boolean eject = false;

        RoomItemFloor item = room.getItems().getFloorItem(id);

        if (!room.getRights().hasRights(client.getPlayer().getId()) && !client.getPlayer().getPermissions().hasPermission("room_full_control")) {
            return;
        }

        if (item == null) {
            RoomItemWall wItem = room.getItems().getWallItem(id);

            if (wItem == null || wItem instanceof PostItWallItem) {
                return;
            }

            if (wItem.getOwner() != client.getPlayer().getId() && !client.getPlayer().getPermissions().hasPermission("room_full_control")) {
                if (wItem.getRoom().getData().getOwnerId() != client.getPlayer().getId() && !client.getPlayer().getPermissions().hasPermission("room_full_control"))
                    return;

                eject = true;
            }

            if (!eject) {
                room.getItems().removeItem(wItem, client.getPlayer().getId(), client);
            } else {
                Session owner = NetworkManager.getInstance().getSessions().getByPlayerId(wItem.getOwner());
                room.getItems().removeItem(wItem, wItem.getOwner(), owner);
            }

            client.send(new RemoveWallItemMessageComposer(wItem.getId(), client.getPlayer().getId()));
            return;
        }

        if (item.getOwner() != client.getPlayer().getId() && !client.getPlayer().getPermissions().hasPermission("room_full_control")) {
            if (item.getRoom().getData().getOwnerId() != client.getPlayer().getId() && !client.getPlayer().getPermissions().hasPermission("room_full_control"))
                return;

            eject = true;
        }

        item.onPickup();

        if (!eject) {
            room.getItems().removeItem(item, client);
        } else {
            Session owner = NetworkManager.getInstance().getSessions().getByPlayerId(item.getOwner());
            room.getItems().removeItem(item, owner);
        }

        client.getPlayer().getQuests().progressQuest(QuestType.FURNI_PICK);
    }
}
