package com.cometproject.server.logging;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.logging.stores.database.DatabaseLogStore;
import com.cometproject.server.logging.stores.memory.MemoryLogStore;
import org.apache.log4j.Logger;

public class LoggingManager {
    private Logger log = Logger.getLogger(LoggingManager.class.getName());

    private AbstractLoggerStore loggerStore;

    public LoggingManager() {
        switch(CometSettings.logStore) {
            case "memory":
            case "internal":
                loggerStore = new MemoryLogStore();

                log.info("Memory logging store is active");
                break;

            case "disabled":
            case "0":
                log.info("Activity logging is disabled");
                break;

            case "service":
            case "external":
                // TODO: Port old code to use the AbstractLoggerStore
                log.info("External logging service is active");
                break;

            case "database":
            case "mysql":
                loggerStore = new DatabaseLogStore();

                log.info("Database logging store is active");
                break;
        }
    }

    public AbstractLoggerStore getStore() {
        return this.loggerStore;
    }
}
