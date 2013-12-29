package com.cometsrv.boot;

import com.cometsrv.config.CometSettings;
import com.cometsrv.config.Configuration;
import com.cometsrv.config.Locale;
import com.cometsrv.game.GameEngine;
import com.cometsrv.game.GameThread;
import com.cometsrv.monitor.SystemMonitor;
import com.cometsrv.network.NetworkEngine;
import com.cometsrv.storage.StorageEngine;

public class CometServer {
    private Configuration config;

    private StorageEngine storageEngine;
    private NetworkEngine networkEngine;
    private SystemMonitor systemMonitor;

    public CometServer() {}

    public void init() {
        loadConfig();
        storageEngine = new StorageEngine();
        systemMonitor = new SystemMonitor();

        Locale.init();
        GameEngine.init();

        networkEngine = new NetworkEngine(this.getConfig().get("comet.network.host"), Integer.parseInt(this.getConfig().get("comet.network.port")));
        GameEngine.gameThread = new GameThread();

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
        return storageEngine;
    }

    public NetworkEngine getNetwork() {
        return this.networkEngine;
    }

    public SystemMonitor getSystemMonitor() {
        return systemMonitor;
    }
}
