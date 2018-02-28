package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class ChangeWallItemStateMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int itemId = msg.readInt();

        RoomItemWall item = client.getPlayer().getEntity().getRoom().getItems().getWallItem(itemId);

        if (item == null) {
            return;
        }

        if (!client.getPlayer().getEntity().isVisible()) {
            return;
        }

        item.onInteract(client.getPlayer().getEntity(), 0, false);
    }
}