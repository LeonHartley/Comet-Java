package com.cometproject.server.network.messages.incoming.room.item.wired;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.items.wired.SaveWiredMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class SaveWiredMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int itemId = msg.readInt();

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        client.getPlayer().getEntity().getRoom().getWired().handleSave(client.getPlayer().getEntity().getRoom().getItems().getFloorItem(itemId), msg);
        client.send(SaveWiredMessageComposer.compose());
    }
}
