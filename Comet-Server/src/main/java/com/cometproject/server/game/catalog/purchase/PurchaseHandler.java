package com.cometproject.server.game.catalog.purchase;

import com.cometproject.api.game.catalog.types.ICatalogItem;
import com.cometproject.server.network.sessions.Session;

public interface PurchaseHandler {
    PurchaseResult handlePurchaseData(Session session, String purchaseData, ICatalogItem catalogItem, int amount);
}
