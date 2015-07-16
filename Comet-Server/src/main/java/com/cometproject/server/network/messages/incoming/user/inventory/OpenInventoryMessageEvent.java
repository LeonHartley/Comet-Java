package com.cometproject.server.network.messages.incoming.user.inventory;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.user.inventory.InventoryMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class OpenInventoryMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        if (!client.getPlayer().getInventory().itemsLoaded()) {
            client.getPlayer().getInventory().loadItems();
        }

        client.send(new InventoryMessageComposer(client.getPlayer().getInventory()));
    }
}
