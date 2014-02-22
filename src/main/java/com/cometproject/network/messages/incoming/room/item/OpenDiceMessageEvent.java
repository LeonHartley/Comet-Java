package com.cometproject.network.messages.incoming.room.item;

import com.cometproject.game.GameEngine;
import com.cometproject.game.rooms.items.FloorItem;
import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class OpenDiceMessageEvent implements IEvent {

    @Override
    public void handle(Session client, Event msg) {
        int itemId = msg.readInt();

        if(client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        FloorItem item = client.getPlayer().getEntity().getRoom().getItems().getFloorItem(itemId);

        if(item == null) {
            return;
        }

        if(item.getDefinition().getInteraction().equals("dice")) {
            GameEngine.getItems().getInteractions().onInteract(0, item, client.getPlayer().getEntity());
        }
    }
}
