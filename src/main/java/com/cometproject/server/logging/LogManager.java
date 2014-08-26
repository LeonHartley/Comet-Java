package com.cometproject.server.logging;

import com.cometproject.server.boot.Comet;
import org.apache.log4j.Logger;

public class LogManager {
    private Logger log = Logger.getLogger(LogManager.class.getName());

    public static final boolean ENABLED = Comet.getServer().getConfig().get("comet.game.logging.enabled").equals("true");

    private LogStore store;

    public LogManager() {
        this.store = new LogStore();
    }

    public LogStore getStore() {
        return store;
    }
}