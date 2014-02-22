package com.cometproject.boot;

import com.cometproject.config.CometSettings;
import com.cometproject.config.Configuration;
import com.cometproject.config.Locale;
import com.cometproject.game.GameEngine;
import com.cometproject.game.GameThread;
import com.cometproject.monitor.SystemMonitor;
import com.cometproject.network.NetworkEngine;
import com.cometproject.storage.StorageEngine;
import com.cometproject.tasks.CometThreadManagement;

public class CometServer {
    private Configuration config;

    private CometThreadManagement threadManagement;

    private StorageEngine storageEngine;
    private NetworkEngine networkEngine;
    private SystemMonitor systemMonitor;

    public CometServer() {}

    public void init() {
        loadConfig();

        threadManagement = new CometThreadManagement();
        storageEngine = new StorageEngine();
        systemMonitor = new SystemMonitor(threadManagement);

        Locale.init();
        GameEngine.init();

        networkEngine = new NetworkEngine(this.getConfig().get("comet.network.host"), Integer.parseInt(this.getConfig().get("comet.network.port")));
        GameEngine.gameThread = new GameThread(threadManagement);

        if(Comet.isDebugging) {
            GameEngine.getLogger().debug("Comet Server is debugging");
        }
    }

    public void loadConfig() {
        config = new Configuration("/comet.properties");
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
}
