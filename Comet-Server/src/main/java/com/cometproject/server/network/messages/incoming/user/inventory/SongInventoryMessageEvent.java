package com.cometproject.server.network.messages.incoming.user.inventory;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.user.inventory.SongInventoryMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;

public class SongInventoryMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        if (!client.getPlayer().getInventory().itemsLoaded()) {
            client.getPlayer().getInventory().loadItems();
        }

        client.send(new SongInventoryMessageComposer(client.getPlayer().getInventory().getSongs()));
    }
}
