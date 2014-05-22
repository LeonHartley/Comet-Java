package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class RunDiceMessageEvent implements IEvent {

    @Override
    public void handle(Session client, Event msg) {
        int itemId = msg.readInt();

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        RoomItemFloor item = client.getPlayer().getEntity().getRoom().getItems().getFloorItem(itemId);

        if (item == null) {
            return;
        }

        item.onInteract(client.getPlayer().getEntity(), -1, false);
    }
}
