package com.cometproject.server.logging;

import com.cometproject.server.logging.containers.RoomVisitContainer;
import com.cometproject.server.logging.database.LogDatabaseManager;
import com.cometproject.server.tasks.CometTask;

public class LogStore implements CometTask {
//    private static final TimeUnit QUEUE_FLUSH_UNIT = TimeUnit.MINUTES;
//    private static final int QUEUE_FLUSH_TIME = 1;

    // Containers
    private RoomVisitContainer roomVisitContainer;

    private LogDatabaseManager logDatabaseManager;

    public LogStore() {
        logDatabaseManager = new LogDatabaseManager();

        // Register the containers
        roomVisitContainer = new RoomVisitContainer(this);
    }

    @Override
    public void run() {
        // For the queue shit...
    }

    public RoomVisitContainer getRoomVisitContainer() {
        return roomVisitContainer;
    }
}
