package com.cometproject.server.logging;

import com.cometproject.server.logging.containers.LogEntryContainer;
import com.cometproject.server.logging.containers.RoomVisitContainer;
import com.cometproject.server.logging.database.LogDatabaseManager;
import com.cometproject.server.tasks.CometTask;

public class LogStore implements CometTask {
//    private static final TimeUnit QUEUE_FLUSH_UNIT = TimeUnit.MINUTES;
//    private static final int QUEUE_FLUSH_TIME = 1;

    // Containers
    private RoomVisitContainer roomVisitContainer;
    private LogEntryContainer logEntryContainer;

    private LogDatabaseManager logDatabaseManager;

    public LogStore() {
        if (!LogManager.ENABLED)
            return;

        logDatabaseManager = new LogDatabaseManager();

        // Register the containers
        roomVisitContainer = new RoomVisitContainer();
        logEntryContainer = new LogEntryContainer();
    }

    @Override
    public void run() {
        // For the queue shit...
    }

    public RoomVisitContainer getRoomVisitContainer() {
        return roomVisitContainer;
    }

    public LogEntryContainer getLogEntryContainer() {
        return logEntryContainer;
    }
}
