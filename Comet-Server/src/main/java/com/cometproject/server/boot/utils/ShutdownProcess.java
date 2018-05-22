package com.cometproject.server.boot.utils;

import com.cometproject.api.game.GameContext;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.logging.LogManager;
import com.cometproject.server.logging.database.queries.LogQueries;
import com.cometproject.server.storage.StorageManager;
import com.cometproject.server.storage.queries.system.StatisticsDao;
import org.apache.log4j.Logger;


public class ShutdownProcess {
    private static final Logger log = Logger.getLogger(ShutdownProcess.class.getName());

    public static void init() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                shutdown(false);
            }
        });
    }

    public static void shutdown(boolean exit) {
        log.info("Comet is now shutting down");

        Comet.isRunning = false;

        log.info("Resetting statistics");
        StatisticsDao.saveStatistics(0, 0, Comet.getBuild());

        if (LogManager.ENABLED) {
            log.info("Updating room entry data");
            LogQueries.updateRoomEntries();
        }

        log.info("Closing all database connections");

        GameContext.setCurrent(null);
        StorageManager.getInstance().shutdown();

        if (exit) {
            System.exit(0);
        }
    }
}
