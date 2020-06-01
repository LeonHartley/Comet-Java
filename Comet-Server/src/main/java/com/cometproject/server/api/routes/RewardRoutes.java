package com.cometproject.server.api.routes;

import com.cometproject.api.game.catalog.types.purchase.CatalogPurchase;
import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.game.furniture.types.GiftData;
import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.api.utilities.JsonUtil;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredUtil;
import com.cometproject.storage.api.StorageContext;
import com.cometproject.storage.api.data.Data;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardRoutes {
    public static Object gift(Request req, Response res) {
        Map<String, Object> result = new HashMap<>();
        res.type("application/json");

        if (!StringUtils.isNumeric(req.params("id")) || !StringUtils.isNumeric(req.queryParams("itemId"))) {
            result.put("error", "Invalid request parameters");
            return result;
        }

        final int playerId = Integer.parseInt(req.params("id"));
        final String sender = req.queryParams("sender");
        final String message = req.queryParams("message");

        final int itemId = Integer.parseInt(req.queryParams("itemId"));

        final PlayerAvatar playerAvatar = PlayerManager.getInstance().getAvatarByPlayerId(playerId, PlayerAvatar.USERNAME_FIGURE);

        if (playerAvatar == null) {
            result.put("error", "Player could not be found");
            return result;
        }

        final Integer giftSprite = WiredUtil.getRandomElement(CatalogManager.getInstance().getGiftBoxesOld());

        if (giftSprite == null) {
            result.put("error", "No gift box available");
            return result;
        }

        FurnitureDefinition giftDefinition = ItemManager.getInstance().getBySpriteId(giftSprite);

        if (giftDefinition == null) {
            result.put("error", "No gift box available");
            return result;
        }


        final String extraData = req.queryParams("itemData") != null ? req.queryParams("itemData") : "0";
        final Data<List<Long>> ids = Data.createEmpty();
        final GiftData giftData = new GiftData(itemId, 0, sender, playerAvatar.getUsername(), message, giftSprite, 0, 0, true, extraData);

        final CatalogPurchase catalogPurchase = new CatalogPurchase(playerId, giftDefinition.getId(),  GiftData.EXTRA_DATA_HEADER + JsonUtil.getInstance().toJson(giftData));
        StorageContext.getCurrentContext().getRoomItemRepository().createItems(Lists.newArrayList(catalogPurchase), ids::set, 0);

        if (!ids.has()) {
            result.put("error", "Unable to create items");
            return result;
        }

        if (PlayerManager.getInstance().isOnline(playerId)) {
            CatalogManager.getInstance().getPurchaseHandler().deliverGift(playerId, giftData, ids.get(), sender);
        }

        result.put("success", true);
        return result;
    }
}
