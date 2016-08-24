package com.cometproject.server.api.routes;

import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.catalog.UnseenItemsMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.network.sessions.SessionManager;
import com.cometproject.server.storage.queries.items.ItemDao;
import com.google.common.collect.Sets;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class PhotoRoutes {
    public static Object purchase(Request req, Response res) {
        Map<String, Object> result = new HashMap<>();
        res.type("application/json");

        final String ssoTicket = req.headers("ssoTicket");
        final String photoId = req.headers("photoId");

        final Integer playerId = PlayerManager.getInstance().getSsoTicketToPlayerId().get(ssoTicket);

        if(playerId == null) {
            result.put("error", "Invalid SSO ticket");
            return result;
        }

        Session client = NetworkManager.getInstance().getSessions().getByPlayerId(playerId);

        if(client == null) {
            result.put("error", "Cannot find Session for player ID: " + playerId);
            return result;
        }

        final long currentTime = System.currentTimeMillis();

        if(currentTime < (client.getPlayer().getLastPhotoTaken() + 10000)) {
            result.put("error", "Taking photos too fast");
            return result;
        }

        final String itemExtraData = "{\"t\":" + System.currentTimeMillis() + ",\"u\":\"" + photoId + "\",\"n\":\"" + client.getPlayer().getData().getUsername() + "\",\"m\":\"\",\"s\":" + client.getPlayer().getId() + ",\"w\":\"" + CometSettings.cameraPhotoUrl.replace("%photoId%", photoId) + "\"}";

        long itemId = ItemDao.createItem(client.getPlayer().getId(), CometSettings.cameraPhotoItemId, itemExtraData);
        final PlayerItem playerItem = new InventoryItem(itemId, CometSettings.cameraPhotoItemId, itemExtraData);

        client.getPlayer().getInventory().addItem(playerItem);

        client.send(new NotificationMessageComposer("generic", Locale.getOrDefault("camera.photoTaken", "You successfully took a photo!")));
        client.send(new UpdateInventoryMessageComposer());

        client.send(new UnseenItemsMessageComposer(Sets.newHashSet(playerItem)));

        client.getPlayer().getAchievements().progressAchievement(AchievementType.CAMERA_PHOTO, 1);
        return result;
    }
}
