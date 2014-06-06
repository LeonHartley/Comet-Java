package com.cometproject.server.logging.stores.database;

import com.cometproject.server.logging.AbstractLogEntry;
import com.cometproject.server.logging.AbstractLoggerStore;
import com.cometproject.server.storage.queries.logging.LoggingDao;
import org.apache.log4j.Logger;

import java.util.List;

public class DatabaseLogStore extends AbstractLoggerStore {
    private Logger log = Logger.getLogger(DatabaseLogStore.class.getName());

    public DatabaseLogStore() {

    }

    @Override
    public List<AbstractLogEntry> getByTimeFrame(long min, long max) {
        return null;
    }

    @Override
    public List<AbstractLogEntry> getByRoomId(int roomId) {
        return null;
    }

    @Override
    public List<AbstractLogEntry> getByUserId(int userId) {
        // Fetch from database
        return null;
    }

    @Override
    public void put(AbstractLogEntry entry) {
        LoggingDao.saveLog(entry);

        log.trace("Log entry was saved to the database, data: " + entry.getString());
    }
}
