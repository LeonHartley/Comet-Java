package com.cometproject.api.game.catalog.types.purchase;

import com.cometproject.api.game.catalog.types.ICatalogPage;
import com.cometproject.api.game.catalog.types.bundles.IRoomBundle;
import com.cometproject.api.game.furniture.types.IGiftData;
import com.cometproject.api.networking.sessions.ISession;

import java.util.List;

public interface ICatalogPurchaseHandler {
    void purchaseItem(ISession client, int pageId, int itemId, String data, int amount, IGiftData giftData);

    void handle(ISession client, int pageId, int itemId, String data, int amount, IGiftData giftData);

    void deliverGift(int playerId, IGiftData giftData, List<Long> newItems, String senderUsername);

    void purchaseBundle(IRoomBundle roomBundle, ISession client);

    void purchaseBundle(ICatalogPage page, ISession client);

    int applyDiscount(int cost, int quantity);
}
