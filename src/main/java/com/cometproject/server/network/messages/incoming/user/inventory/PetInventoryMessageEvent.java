package com.cometproject.server.network.messages.incoming.user.inventory;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.user.inventory.PetInventoryMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class PetInventoryMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        client.send(PetInventoryMessageComposer.compose(client.getPlayer().getPets().getPets()));
    }
}
