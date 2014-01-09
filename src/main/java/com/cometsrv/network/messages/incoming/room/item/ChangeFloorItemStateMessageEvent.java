package com.cometsrv.network.messages.incoming.room.item;

import com.cometsrv.game.GameEngine;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class ChangeFloorItemStateMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int itemId = msg.readInt();

        if(client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        FloorItem item = client.getPlayer().getEntity().getRoom().getItems().getFloorItem(itemId);

        if(item == null) {
            return;
        }

        // Can't close gate when a user is on same tile?
        if (item.getDefinition().getInteraction().equals("gate")
                && client.getPlayer().getEntity().getRoom().getEntities().getEntitiesAt(item.getX(), item.getY()).size() > 0) {
            return;
        }

        GameEngine.getItems().getInteractions().onInteract(0, item, client.getPlayer().getEntity());
    }
}
