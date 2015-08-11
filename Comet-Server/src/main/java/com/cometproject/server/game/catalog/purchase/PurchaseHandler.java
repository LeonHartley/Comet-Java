package com.cometproject.server.game.catalog.purchase;

import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.network.sessions.Session;

public interface PurchaseHandler {
    PurchaseResult handlePurchaseData(Session session, String purchaseData, CatalogItem catalogItem, int amount);
}
