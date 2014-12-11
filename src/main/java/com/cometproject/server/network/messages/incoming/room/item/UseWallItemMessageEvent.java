package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class UseWallItemMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        int itemId = msg.readInt();

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        RoomItemWall item = client.getPlayer().getEntity().getRoom().getItems().getWallItem(itemId);

        if (item == null) {
            return;
        }

        int requestData = msg.readInt();

        item.onInteract(client.getPlayer().getEntity(), requestData, false);
    }
}
