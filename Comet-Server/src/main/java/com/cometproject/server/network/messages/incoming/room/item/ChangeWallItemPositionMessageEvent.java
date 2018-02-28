package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateWallItemMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.storage.api.StorageContext;


public class ChangeWallItemPositionMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        int virtualId = msg.readInt();

        Long itemId = ItemManager.getInstance().getItemIdByVirtualId(virtualId);

        if (itemId == null) {
            return;
        }

        String position = Position.validateWallPosition(msg.readString());

        Room room = client.getPlayer().getEntity().getRoom();

        if (room == null || position == null) {
            return;
        }

        boolean isOwner = client.getPlayer().getId() == room.getData().getOwnerId() || client.getPlayer().getData().getRank() > 5;
        boolean hasRights = room.getRights().hasRights(client.getPlayer().getId());

        if (isOwner || hasRights || client.getPlayer().getPermissions().getRank().roomFullControl()) {
            RoomItemWall item = room.getItems().getWallItem(itemId);

            if (item == null) {
                return;
            }

            StorageContext.getCurrentContext().getRoomItemRepository().placeWallItem(room.getId(), position, (item.getItemData().getData().isEmpty() || item.getItemData().getData().equals(" ")) ? "0" : item.getItemData().getData(), item.getId());

            item.setWallPosition(position);
            room.getEntities().broadcastMessage(new UpdateWallItemMessageComposer(item, room.getData().getOwnerId(), room.getData().getOwner()));
        }
    }
}
