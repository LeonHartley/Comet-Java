package com.cometproject.server.network.messages.incoming.user.inventory;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.user.inventory.InventoryMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class OpenInventoryMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(InventoryMessageComposer.compose(client.getPlayer().getInventory()));
    }
}
