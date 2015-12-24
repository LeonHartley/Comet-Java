package com.cometproject.server.boot;

import com.cometproject.server.api.APIManager;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Configuration;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.GameThread;
import com.cometproject.server.game.achievements.AchievementManager;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.commands.CommandManager;
import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.landing.LandingManager;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.moderation.ModerationManager;
import com.cometproject.server.game.navigator.NavigatorManager;
import com.cometproject.server.game.permissions.PermissionsManager;
import com.cometproject.server.game.pets.PetManager;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.polls.PollManager;
import com.cometproject.server.game.quests.QuestManager;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.bundles.RoomBundleManager;
import com.cometproject.server.game.utilities.validator.PlayerFigureValidator;
import com.cometproject.server.logging.LogManager;
import com.cometproject.server.modules.ModuleManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.storage.StorageManager;
import com.cometproject.server.storage.queue.types.ItemStorageQueue;
import com.cometproject.server.storage.queue.types.PlayerDataStorageQueue;
import com.cometproject.server.tasks.CometThreadManager;
import org.apache.log4j.Logger;

import java.util.Map;


public class CometServer {
    private final Logger log = Logger.getLogger(CometServer.class.getName());

    /**
     * Comet's configuration
     */
    private Configuration config;

    public CometServer(Map<String, String> overridenConfig) {
        this.config = new Configuration("./config/comet.properties");

        if (overridenConfig != null) {
            this.config.override(overridenConfig);
        }
    }

    /**
     * Initialize Comet Server
     */
    public void init() {
        ModuleManager.getInstance().initialize();
        APIManager.getInstance().initialize();
//        WebSocketServer.getInstance().initialize();
        PlayerFigureValidator.loadFigureData();

        CometThreadManager.getInstance().initialize();
        StorageManager.getInstance().initialize();
        LogManager.getInstance().initialize();

        // Locale & config
        CometSettings.initialize();
        Locale.initialize();

        // Initialize the game managers
        // TODO: Implement some sort of dependency injection so we don't need any of this crap!!

        PermissionsManager.getInstance().initialize();
        RoomBundleManager.getInstance().initialize();
        ItemManager.getInstance().initialize();
        CatalogManager.getInstance().initialize();
        RoomManager.getInstance().initialize();
        NavigatorManager.getInstance().initialize();
        CommandManager.getInstance().initialize();
        BanManager.getInstance().initialize();
        ModerationManager.getInstance().initialize();
        PetManager.getInstance().initialize();
        LandingManager.getInstance().initialize();
        GroupManager.getInstance().initialize();
        PlayerManager.getInstance().initialize();
        QuestManager.getInstance().initialize();
        AchievementManager.getInstance().initialize();
        PollManager.getInstance().initialize();

        PlayerDataStorageQueue.getInstance().initialize();
        ItemStorageQueue.getInstance().initialize();

        String ipAddress = this.getConfig().get("comet.network.host"),
                port = this.getConfig().get("comet.network.port");

        PlayerFigureValidator.isValidFigureCode("ea-3270-63-1408.lg-3364-64-1408.ca-3131-63-1408.wa-3359-63.ch-210-1424.fa-3193-92.hr-135-31.cc-3374-63.sh-3348-91.cp-3308-63-1408.hd-207-1362.he-3082-63", "m");

        NetworkManager.getInstance().initialize(ipAddress, port);
        GameThread.getInstance().initialize();

        if (Comet.isDebugging) {
            log.debug("Comet Server is debugging");
        }
    }

    /**
     * Get the Comet configuration
     *
     * @return Comet configuration
     */
    public Configuration getConfig() {
        return this.config;
    }

    public Logger getLogger() {
        return log;
    }
}
