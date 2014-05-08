package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.items.FloorItem;
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

        FloorItem item = client.getPlayer().getEntity().getRoom().getItems().getFloorItem(itemId);

        if (item == null) {
            return;
        }

        if (item.getDefinition().getInteraction().equals("dice")) {
            CometManager.getItems().getInteractions().onInteract(-1, item, client.getPlayer().getEntity());
        }
    }
}
