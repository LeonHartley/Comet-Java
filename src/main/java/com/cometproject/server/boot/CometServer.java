package com.cometproject.server.boot;

import com.cometproject.server.cache.CometCache;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Configuration;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.GameThread;
import com.cometproject.server.monitor.SystemMonitor;
import com.cometproject.server.network.NetworkEngine;
import com.cometproject.server.plugins.PluginEngine;
import com.cometproject.server.storage.StorageEngine;
import com.cometproject.server.tasks.CometThreadManagement;
import com.cometproject.server.game.rooms.avatars.misc.FilterManager;

public class CometServer {
    private Configuration config;

    private CometThreadManagement threadManagement;

    private StorageEngine storageEngine;
    private PluginEngine pluginEngine;
    private NetworkEngine networkEngine;
    private SystemMonitor systemMonitor;

    public CometServer() {}

    public void init() {
        loadConfig();

        threadManagement = new CometThreadManagement();
        storageEngine = new StorageEngine();
        pluginEngine = new PluginEngine();
        systemMonitor = new SystemMonitor(threadManagement);

        Locale.init();
        GameEngine.init();
        CometCache.create();

        networkEngine = new NetworkEngine(this.getConfig().get("comet.network.host"), Integer.parseInt(this.getConfig().get("comet.network.port")));
        GameEngine.gameThread = new GameThread(threadManagement);

        if(Comet.isDebugging) {
            GameEngine.getLogger().debug("Comet Server is debugging");
        }
    }

    public void loadConfig() {
        config = new Configuration("./config/comet.properties");
        CometSettings.set(config.getProperties());
    }

    public Configuration getConfig() {
        return this.config;
    }

    public StorageEngine getStorage() {
        return this.storageEngine;
    }

    public NetworkEngine getNetwork() {
        return this.networkEngine;
    }

    public SystemMonitor getSystemMonitor() {
        return this.systemMonitor;
    }

    public CometThreadManagement getThreadManagement() { return this.threadManagement; }

    public PluginEngine getPluginEngine() {
        return pluginEngine;
    }
}
