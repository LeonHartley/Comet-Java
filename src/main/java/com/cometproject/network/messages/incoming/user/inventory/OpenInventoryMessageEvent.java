package com.cometproject.network.messages.incoming.user.inventory;

import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.user.inventory.InventoryMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class OpenInventoryMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(InventoryMessageComposer.compose(client.getPlayer().getInventory()));
    }
}
