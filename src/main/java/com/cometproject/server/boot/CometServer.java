package com.cometproject.server.boot;

import com.cometproject.server.api.APIManager;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Configuration;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.logging.LogManager;
import com.cometproject.server.logging.sentry.SentryDispatcher;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.plugins.PluginManager;
import com.cometproject.server.storage.StorageManager;
import com.cometproject.server.storage.helpers.SqlIndexChecker;
import com.cometproject.server.tasks.CometThreadManagement;
import net.kencochrane.raven.event.Event;
import net.kencochrane.raven.event.EventBuilder;

import java.util.Map;

public class CometServer {
    /**
     * Comet's configuration
     */
    private Configuration config;

    /**
     * The main Comet Server thread manager
     */
    private CometThreadManagement threadManagement;

    /**
     * The API manager
     */
    private APIManager apiManager;

    /**
     * MySQL Storage manager
     */
    private StorageManager storageManager;

    /**
     * JavaScript plugin manager
     */
    private PluginManager pluginManager;

    /**
     * Networking manager
     */
    private NetworkManager networkManager;

    /**
     * Logging manager
     */
    private LogManager loggingManager;

    /**
     * Empty constructor
     */
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
        this.apiManager = new APIManager();

        this.threadManagement = new CometThreadManagement();
        this.storageManager = new StorageManager();
        this.pluginManager = new PluginManager();

        this.loggingManager = new LogManager();

//        SqlIndexChecker.checkIndexes(storageManager);
//        SqlIndexChecker.setIndexes(storageManager);

        CometSettings.init();
        Locale.init();
        CometManager.init();

        String ipAddress = this.getConfig().get("comet.network.host"),
               port = this.getConfig().get("comet.network.port");

        this.networkManager = new NetworkManager(ipAddress, port);

        CometManager.startCycle();

//        SentryDispatcher.getInstance().dispatchManualEvent(
//                new EventBuilder()
//                        .addTag("type", "boot")
//                        .setMessage("Comet Server was booted")
//                        .addExtra("Server IP", ipAddress)
//                        .addExtra("Server Port", port)
//                        .setLevel(Event.Level.INFO)
//                        .setLogger("INFO"),
//                false);

        if (Comet.isDebugging) {
            CometManager.getLogger().debug("Comet Server is debugging");
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

    /**
     * Get the MySQL storage manager
     *
     * @return MySQL Storage manager
     */
    public StorageManager getStorage() {
        return this.storageManager;
    }

    /**
     * Get the networking manager
     *
     * @return The networking manager
     */
    public NetworkManager getNetwork() {
        return this.networkManager;
    }

    /**
     * Get the threading manager
     *
     * @return The threading manager
     */
    public CometThreadManagement getThreadManagement() {
        return this.threadManagement;
    }

    /**
     * Get the plugin manager
     *
     * @return The plugin manager
     */
    public PluginManager getPlugins() {
        return pluginManager;
    }

    /**
     * Get the logging manager
     *
     * @return The logging manager
     */
    public LogManager getLoggingManager() {
        return loggingManager;
    }

    public APIManager getAPI() {
        return apiManager;
    }
}
