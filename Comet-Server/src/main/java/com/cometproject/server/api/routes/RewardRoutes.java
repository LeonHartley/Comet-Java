package com.cometproject.server.api.routes;

import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.inventory.InventoryDao;
import org.apache.commons.lang.StringUtils;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class RewardRoutes {
    public static Object gift(Request req, Response res) {
        Map<String, Object> result = new HashMap<>();
        res.type("application/json");

        if (!StringUtils.isNumeric(req.params("id"))) {
            result.put("error", "Invalid ID");
            return result;
        }

        int playerId = Integer.parseInt(req.params("id"));
        String badgeId = req.params("badge");

        if (!PlayerManager.getInstance().isOnline(playerId)) {
            if (badgeId != null) {
                InventoryDao.addBadge(badgeId, playerId);
            }
        } else {
            Session session = NetworkManager.getInstance().getSessions().getByPlayerId(playerId);

            if (badgeId != null && session != null) {
                if (!session.getPlayer().getInventory().hasBadge(badgeId))
                    session.getPlayer().getInventory().addBadge(badgeId, true);
            }
        }

        result.put("success", true);
        return result;
    }
}
