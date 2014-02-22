package com.cometproject.server.network.messages.incoming.user.inventory;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.user.inventory.BadgeInventoryMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class BadgeInventoryMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(BadgeInventoryMessageComposer.compose(client.getPlayer().getInventory().getBadges()));
    }
}
