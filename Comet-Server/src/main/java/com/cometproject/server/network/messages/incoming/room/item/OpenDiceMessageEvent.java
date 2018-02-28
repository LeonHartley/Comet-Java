package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class OpenDiceMessageEvent implements Event {

    @Override
    public void handle(Session client, MessageEvent msg) {
        int virtualId = msg.readInt();

        long itemId = ItemManager.getInstance().getItemIdByVirtualId(virtualId);

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        RoomItemFloor item = client.getPlayer().getEntity().getRoom().getItems().getFloorItem(itemId);

        if (item == null) {
            return;
        }

        if (!client.getPlayer().getEntity().isVisible()) {
            return;
        }

        item.onInteract(client.getPlayer().getEntity(), -1, false);
    }
}
