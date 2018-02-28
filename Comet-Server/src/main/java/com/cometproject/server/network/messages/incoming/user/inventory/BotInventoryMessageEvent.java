package com.cometproject.server.network.messages.incoming.user.inventory;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.user.inventory.BotInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class BotInventoryMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        client.send(new BotInventoryMessageComposer(client.getPlayer().getBots().getBots()));
    }
}
