package com.cometproject.server.logging;

import org.apache.log4j.Logger;

public class LogManager {
    private Logger log = Logger.getLogger(LogManager.class.getName());

    private LogStore store;

    public LogManager() {
        this.store = new LogStore();
    }

    public LogStore getStore() {
        return store;
    }
}
