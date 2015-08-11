package com.cometproject.server.game.catalog.purchase.handlers;

import com.cometproject.server.game.catalog.purchase.PurchaseHandler;
import com.cometproject.server.game.catalog.purchase.PurchaseResult;
import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.network.sessions.Session;

public class StickiesPurchaseHandler implements PurchaseHandler {
    @Override
    public PurchaseResult handlePurchaseData(Session session, String purchaseData, CatalogItem catalogItem, int amount) {
        return new PurchaseResult(amount * 20, "");
    }
}
