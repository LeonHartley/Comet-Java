package com.cometsrv.network.messages.incoming.room.item;

import com.cometsrv.game.GameEngine;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class ChangeFloorItemStateMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int itemId = msg.readInt();

        if(client.getPlayer().getAvatar() == null || client.getPlayer().getAvatar().getRoom() == null) {
            return;
        }

        FloorItem item = client.getPlayer().getAvatar().getRoom().getItems().getFloorItem(itemId);

        if(item == null) {
            return;
        }

        if(item.getDefinition().getInteraction().equals("gate") && client.getPlayer().getAvatar().getRoom().getAvatars().getAvatarAt(item.getX(), item.getY()) != null) {
            return;
        }

        GameEngine.getItems().getInteractions().onInteract(0, item, client.getPlayer().getAvatar());
    }
}
