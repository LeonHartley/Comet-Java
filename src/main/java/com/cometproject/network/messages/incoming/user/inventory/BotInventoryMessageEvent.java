package com.cometproject.network.messages.incoming.user.inventory;

import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.user.inventory.BotInventoryMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class BotInventoryMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(BotInventoryMessageComposer.compose(client.getPlayer().getBots().getBots()));
    }
}
