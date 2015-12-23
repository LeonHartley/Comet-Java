package com.cometproject.server.boot.utils;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.storage.queue.types.ItemStorageQueue;
import com.cometproject.server.storage.queue.types.PlayerDataStorageQueue;
import com.cometproject.server.logging.LogManager;
import com.cometproject.server.logging.database.queries.LogQueries;
import com.cometproject.server.storage.StorageManager;
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

                PlayerDataStorageQueue.getInstance().shutdown();
                ItemStorageQueue.getInstance().shutdown();

                log.info("Resetting statistics");
                StatisticsDao.saveStatistics(0, 0, Comet.getBuild());

                if (LogManager.ENABLED) {
                    log.info("Updating room entry data");
                    LogQueries.updateRoomEntries();
                }

                log.info("Closing all database connections");
                StorageManager.getInstance().getConnections().shutdown();
            }
        });
    }
}
