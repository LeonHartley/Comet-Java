package com.cometproject.server.logging.containers;

import com.cometproject.server.logging.AbstractLogEntry;
import com.cometproject.server.logging.database.queries.LogQueries;

public class LogEntryContainer {
    // TODO: Queue these and insert into database periodically
    public void put(AbstractLogEntry logEntry) {
        LogQueries.putEntry(logEntry);
    }
}
