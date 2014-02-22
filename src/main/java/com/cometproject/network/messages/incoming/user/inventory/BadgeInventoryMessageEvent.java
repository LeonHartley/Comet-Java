package com.cometproject.network.messages.incoming.user.inventory;

import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.user.inventory.BadgeInventoryMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class BadgeInventoryMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(BadgeInventoryMessageComposer.compose(client.getPlayer().getInventory().getBadges()));
    }
}
