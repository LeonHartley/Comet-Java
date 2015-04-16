package com.cometproject.server.network.messages.incoming.catalog;

import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.catalog.types.gifts.GiftData;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class PurchaseGiftMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int pageId = msg.readInt();
        int itemId = msg.readInt();

        String extraData = msg.readString();

        String sendingUser = msg.readString();
        String message = msg.readString();
        int spriteId = msg.readInt();
        int wrappingPaper = msg.readInt();
        int decorationType = msg.readInt();
        boolean showUsername = msg.readBoolean();

        GiftData data = new GiftData(pageId, itemId, client.getPlayer().getId(), sendingUser, message, spriteId, wrappingPaper, decorationType, showUsername, extraData);

        CatalogManager.getInstance().getPurchaseHandler().handle(client, pageId, itemId, extraData, 1, data);
    }
}
