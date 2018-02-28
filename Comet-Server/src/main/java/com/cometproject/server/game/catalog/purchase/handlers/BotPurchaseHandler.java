package com.cometproject.server.game.catalog.purchase.handlers;

import com.cometproject.api.game.catalog.types.ICatalogItem;
import com.cometproject.server.game.catalog.purchase.PurchaseHandler;
import com.cometproject.server.game.catalog.purchase.PurchaseResult;
import com.cometproject.server.network.sessions.Session;

public class BotPurchaseHandler implements PurchaseHandler {
    @Override
    public PurchaseResult handlePurchaseData(Session session, String purchaseData, ICatalogItem catalogItem, int amount) {
        return null;
    }
}
