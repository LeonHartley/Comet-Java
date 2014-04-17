package com.cometproject.server.network.messages.incoming.catalog;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class PurchaseItemMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int pageId = msg.readInt();
        int itemId = msg.readInt();
        String data = msg.readString();
        int amount = msg.readInt();

        GameEngine.getCatalog().getPurchaseHandler().handle(client, pageId, itemId, data, amount, null);
    }
}
