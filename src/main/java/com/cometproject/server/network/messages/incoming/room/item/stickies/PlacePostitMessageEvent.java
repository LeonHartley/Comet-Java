package com.cometproject.server.network.messages.incoming.room.item.stickies;

import com.cometproject.server.game.players.components.types.InventoryItem;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class PlacePostItMessageEvent implements IEvent {

    @Override
    public void handle(Session client, Event msg) throws Exception {
        int itemId = msg.readInt();

        if (client.getPlayer() == null || client.getPlayer().getEntity() == null) {
            return;
        }

        String position = Position.validateWallPosition(msg.readString());

        if (!client.getPlayer().getEntity().getRoom().getRights().hasRights(client.getPlayer().getId())
                && !client.getPlayer().getPermissions().hasPermission("room_full_control")) {
            return;
        }

        if (position == null) {
            return;
        }

        InventoryItem item = client.getPlayer().getInventory().getWallItem(itemId);

        if (item == null) {
            return;
        }

        client.getPlayer().getEntity().getRoom().getItems().placeWallItem(item, position, client.getPlayer());
    }
}
