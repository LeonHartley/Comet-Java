package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.rooms.entities.misc.Position3D;
import com.cometproject.server.game.rooms.items.RoomItemWall;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateWallItemMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;

public class ChangeWallItemPositionMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int itemId = msg.readInt();
        String position = Position3D.validateWallPosition(msg.readString());

        Room room = client.getPlayer().getEntity().getRoom();

        if (room == null || position == null) {
            return;
        }

        boolean isOwner = client.getPlayer().getId() == room.getData().getOwnerId() || client.getPlayer().getData().getRank() > 5;
        boolean hasRights = room.getRights().hasRights(client.getPlayer().getId());

        if (isOwner || hasRights || client.getPlayer().getPermissions().hasPermission("room_full_control")) {
            RoomItemWall item = room.getItems().getWallItem(itemId);

            if (item == null) {
                return;
            }

            RoomItemDao.placeWallItem(room.getId(), position, (item.getExtraData().isEmpty() || item.getExtraData().equals(" ")) ? "0" : item.getExtraData(), item.getId());

            item.setPosition(position);
            room.getEntities().broadcastMessage(UpdateWallItemMessageComposer.compose(item, room.getData().getOwnerId(), room.getData().getOwner()));
        }
    }
}
