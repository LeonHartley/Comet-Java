package com.cometproject.server.logging;

import com.cometproject.api.config.Configuration;
import com.cometproject.api.utilities.Initialisable;
import org.apache.log4j.Logger;


public class LogManager implements Initialisable {
    public static final boolean ENABLED = Configuration.currentConfig().get("comet.game.logging.enabled").equals("true");
    private static LogManager logManagerInstance;
    private Logger log = Logger.getLogger(LogManager.class.getName());
    private LogStore store;

    public LogManager() {
    }

    public static LogManager getInstance() {
        if (logManagerInstance == null) {
            logManagerInstance = new LogManager();
        }

        return logManagerInstance;
    }

    @Override
    public void initialize() {
        this.store = new LogStore();
    }

    public LogStore getStore() {
        return store;
    }
}