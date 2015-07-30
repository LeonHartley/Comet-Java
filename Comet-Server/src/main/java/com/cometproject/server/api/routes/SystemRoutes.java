package com.cometproject.server.api.routes;

import com.cometproject.api.networking.sessions.ISession;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.commands.CommandManager;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.landing.LandingManager;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.moderation.ModerationManager;
import com.cometproject.server.game.navigator.NavigatorManager;
import com.cometproject.server.game.permissions.PermissionsManager;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.catalog.CatalogPublishMessageComposer;
import com.cometproject.server.network.messages.outgoing.moderation.ModToolMessageComposer;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class SystemRoutes {
    public static Object reload(Request req, Response res) {
        Map<String, Object> result = new HashMap<>();
        res.type("application/json");

        String type = req.params("type");

        if (type == null) {
            result.put("error", "Invalid type");
            return result;
        }

        switch (type) {
            case "bans":
                BanManager.getInstance().loadBans();
                break;

            case "catalog":
                CatalogManager.getInstance().loadPages();
                CatalogManager.getInstance().loadGiftBoxes();

                NetworkManager.getInstance().getSessions().broadcast(new CatalogPublishMessageComposer(true));
                break;

            case "navigator":
                NavigatorManager.getInstance().loadFeaturedRooms();
                break;

            case "permissions":
                PermissionsManager.getInstance().loadRankPermissions();
                PermissionsManager.getInstance().loadPerks();
                PermissionsManager.getInstance().loadCommands();
                break;

            case "config":
                CometSettings.initialize();
                break;

            case "news":
                LandingManager.getInstance().loadArticles();
                break;

            case "items":
                ItemManager.getInstance().loadItemDefinitions();
                break;

            case "filter":
                RoomManager.getInstance().getFilter().loadFilter();
                break;

            case "locale":
                Locale.reload();
                CommandManager.getInstance().reloadAllCommands();
                break;

            case "modpresets":
                ModerationManager.getInstance().loadPresets();

                for (ISession session : NetworkManager.getInstance().getSessions().getByPlayerPermission("mod_tool")) {
                    session.send(new ModToolMessageComposer());
                }

                break;
        }

        result.put("success", true);
        return result;
    }

}
