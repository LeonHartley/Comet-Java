package com.cometproject.server.boot;

import com.cometproject.api.config.Configuration;
import com.cometproject.api.game.GameContext;
import com.cometproject.server.api.APIManager;
import com.cometproject.server.boot.utils.gui.CometGui;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.GameCycle;
import com.cometproject.server.game.achievements.AchievementManager;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.commands.CommandManager;
import com.cometproject.server.game.groups.items.GroupItemManager;
import com.cometproject.server.game.guides.GuideManager;
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
import com.cometproject.server.network.messages.incoming.room.action.TalkMessageEvent;
import com.cometproject.server.storage.StorageManager;
import com.cometproject.server.storage.queries.config.ConfigDao;
import com.cometproject.server.tasks.CometThreadManager;
import org.apache.log4j.Logger;

import java.util.Map;


public class CometServer {
    public static final String CLIENT_VERSION = "PRODUCTION-201709192204-203982672";
    private final Logger log = Logger.getLogger(CometServer.class.getName());

    public CometServer(Map<String, String> overridenConfig) {
        Configuration.setConfiguration(new Configuration("./config/comet.properties"));

        if (overridenConfig != null) {
            Configuration.currentConfig().override(overridenConfig);
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
        ConfigDao.getAll();

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
        PlayerManager.getInstance().initialize();
        QuestManager.getInstance().initialize();
        AchievementManager.getInstance().initialize();
        PollManager.getInstance().initialize();
        GuideManager.getInstance().initialize();

        GameContext gameContext = new GameContext();

        gameContext.setCatalogService(CatalogManager.getInstance());
        gameContext.setFurnitureService(ItemManager.getInstance());
        gameContext.setPlayerService(PlayerManager.getInstance());

        GameContext.setCurrent(gameContext);

        String ipAddress = this.getConfig().get("comet.network.host"),
                port = this.getConfig().get("comet.network.port");

        NetworkManager.getInstance().initialize(ipAddress, port);


        ModuleManager.getInstance().setupModules();

        GameContext.getCurrent().getGroupService().setItemService(new GroupItemManager());

        GameCycle.getInstance().initialize();

        if (Comet.showGui) {
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
        return Configuration.currentConfig();
    }

    public Logger getLogger() {
        return log;
    }
}
