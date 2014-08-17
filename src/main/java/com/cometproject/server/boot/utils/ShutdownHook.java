package com.cometproject.server.boot.utils;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.logging.database.queries.LogQueries;
import com.cometproject.server.storage.queries.system.StatisticsDao;
import org.apache.log4j.Logger;

public class ShutdownHook {
    private static final Logger log = Logger.getLogger(ShutdownHook.class.getName());

    public static void init() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                log.info("Comet is now shutting down");

                Comet.isRunning = false;

                StatisticsDao.saveStatistics(0, 0, Comet.getBuild());
                LogQueries.updateRoomEntries();

                log.info("Closing all database connections");
                Comet.getServer().getStorage().getConnections().shutdown();
            }
        });
    }
}
