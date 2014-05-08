package com.cometproject.server.boot;

import com.cometproject.server.cache.CometCache;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Configuration;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.GameThread;
import com.cometproject.server.network.NetworkEngine;
import com.cometproject.server.plugins.PluginEngine;
import com.cometproject.server.storage.SqlStorageEngine;
import com.cometproject.server.tasks.CometThreadManagement;

public class CometServer {
    private Configuration config;

    private CometThreadManagement threadManagement;

    private SqlStorageEngine storageEngine;
    private PluginEngine pluginEngine;
    private NetworkEngine networkEngine;

    public CometServer() {
    }

    public void init() {
        loadConfig();

        threadManagement = new CometThreadManagement();
        storageEngine = new SqlStorageEngine();
        pluginEngine = new PluginEngine();

        Locale.init();
        CometManager.init();
        CometCache.create();

        networkEngine = new NetworkEngine(this.getConfig().get("comet.network.host"), Integer.parseInt(this.getConfig().get("comet.network.port")));
        CometManager.gameThread = new GameThread(threadManagement);

        if (Comet.isDebugging) {
            CometManager.getLogger().debug("Comet Server is debugging");
        }
    }

    public void loadConfig() {
        config = new Configuration("./config/comet.properties");
        CometSettings.set(config.getProperties());
    }

    public Configuration getConfig() {
        return this.config;
    }

    public SqlStorageEngine getStorage() {
        return this.storageEngine;
    }

    public NetworkEngine getNetwork() {
        return this.networkEngine;
    }

    public CometThreadManagement getThreadManagement() {
        return this.threadManagement;
    }

    public PluginEngine getPluginEngine() {
        return pluginEngine;
    }
}
