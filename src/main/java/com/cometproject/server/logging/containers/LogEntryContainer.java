package com.cometproject.server.logging.containers;

import com.cometproject.server.logging.AbstractLogEntry;
import com.cometproject.server.logging.LogStore;
import com.cometproject.server.logging.database.queries.LogQueries;

public class LogEntryContainer {
    public void put(AbstractLogEntry logEntry) {
        LogQueries.putEntry(logEntry);
    }
}
