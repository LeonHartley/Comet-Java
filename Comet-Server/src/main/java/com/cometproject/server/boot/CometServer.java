package com.cometproject.server.boot;

import com.cometproject.server.api.APIManager;
import com.cometproject.server.boot.utils.gui.CometGui;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Configuration;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.GameCycle;
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

    public static final String CLIENT_VERSION = "PRODUCTION-201607262204-86871104 - Modified by trylix";

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
        ModuleManager.getInstance().initialise();
        APIManager.getInstance().initialise();
//        WebSocketServer.getInstance().initialise();
        PlayerFigureValidator.loadFigureData();

        CometThreadManager.getInstance().initialise();
        StorageManager.getInstance().initialise();
        LogManager.getInstance().initialise();

        // Locale & config
        CometSettings.initialize();
        Locale.initialize();

        // Initialize the game managers
        // TODO: Implement some sort of dependency injection so we don't need any of this crap!!

        PermissionsManager.getInstance().initialise();
        RoomBundleManager.getInstance().initialize();
        ItemManager.getInstance().initialise();
        CatalogManager.getInstance().initialise();
        RoomManager.getInstance().initialise();
        NavigatorManager.getInstance().initialise();
        CommandManager.getInstance().initialise();
        BanManager.getInstance().initialise();
        ModerationManager.getInstance().initialise();
        PetManager.getInstance().initialise();
        LandingManager.getInstance().initialise();
        PlayerManager.getInstance().initialise();
        GroupManager.getInstance().initialise();
        QuestManager.getInstance().initialise();
        AchievementManager.getInstance().initialise();
        PollManager.getInstance().initialise();

        PlayerDataStorageQueue.getInstance().initialise();
        ItemStorageQueue.getInstance().initialise();

        String ipAddress = this.getConfig().get("comet.network.host"),
                port = this.getConfig().get("comet.network.port");

        NetworkManager.getInstance().initialize(ipAddress, port);
        GameCycle.getInstance().initialise();

        if(Comet.showGui) {
            CometGui gui = new CometGui();
            gui.setVisible(true);
        }

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
