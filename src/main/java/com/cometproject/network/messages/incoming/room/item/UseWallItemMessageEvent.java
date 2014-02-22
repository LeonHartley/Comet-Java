package com.cometproject.network.messages.incoming.room.item;

import com.cometproject.game.GameEngine;
import com.cometproject.game.rooms.items.WallItem;
import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class UseWallItemMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        int itemId = msg.readInt();

        if(client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        WallItem item = client.getPlayer().getEntity().getRoom().getItems().getWallItem(itemId);

        if(item == null) {
            return;
        }

        int requestData = msg.readInt();

        GameEngine.getItems().getInteractions().onInteract(requestData, item, client.getPlayer().getEntity());
    }
}
