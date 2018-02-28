package com.cometproject.server.network.messages.incoming.catalog;

import com.cometproject.api.game.catalog.types.ICatalogOffer;
import com.cometproject.api.game.furniture.types.GiftData;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class PurchaseGiftMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int pageId = msg.readInt();
        int itemId = msg.readInt();

        if (pageId <= 0) {
            final ICatalogOffer catalogOffer = CatalogManager.getInstance().getCatalogOffers().get(itemId);

            if (catalogOffer == null) {
                return;
            }

            pageId = catalogOffer.getCatalogPageId();
            itemId = catalogOffer.getCatalogItemId();
        }

        String extraData = msg.readString();

        String sendingUser = msg.readString();
        String message = msg.readString();
        int spriteId = msg.readInt();
        int wrappingPaper = msg.readInt();
        int decorationType = msg.readInt();
        boolean showUsername = msg.readBoolean();

        if (!CatalogManager.getInstance().getGiftBoxesNew().contains(spriteId) && !CatalogManager.getInstance().getGiftBoxesOld().contains(spriteId)) {
            client.disconnect();
            return;
        }

        GiftData data = new GiftData(pageId, itemId, client.getPlayer().getId(), sendingUser, message, spriteId, wrappingPaper, decorationType, showUsername, extraData);

        CatalogManager.getInstance().getPurchaseHandler().purchaseItem(client, pageId, itemId, extraData, 1, data);
    }
}
