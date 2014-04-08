package com.cometproject.server.network.messages.incoming.catalog;

import com.cometproject.server.game.catalog.types.gifts.GiftData;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class PurchaseGiftMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int pageId = msg.readInt();
        int itemId = msg.readInt();

        msg.readString();

        String sendingUser = msg.readString();
        String message = msg.readString();
        int spriteId = msg.readInt();
        int wrappingPaper = msg.readInt();
        int decorationType = msg.readInt();
        boolean showUsername = msg.readBoolean();

        GiftData data = new GiftData(pageId, itemId, sendingUser, message, spriteId, wrappingPaper, decorationType, showUsername);
        System.out.println(spriteId);
        System.out.println("===");
        System.out.println(data.toString(client.getPlayer().getId()));
    }
}
