package com.cometsrv.network.messages.incoming.room.item;

import com.cometsrv.game.GameEngine;
import com.cometsrv.game.rooms.items.WallItem;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class UseWallItemMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        int itemId = msg.readInt();

        if(client.getPlayer().getAvatar() == null || client.getPlayer().getAvatar().getRoom() == null) {
            return;
        }

        WallItem item = client.getPlayer().getAvatar().getRoom().getItems().getWallItem(itemId);

        if(item == null) {
            return;
        }

        int requestData = msg.readInt();

        GameEngine.getItems().getInteractions().onInteract(requestData, item, client.getPlayer().getAvatar());
    }
}
